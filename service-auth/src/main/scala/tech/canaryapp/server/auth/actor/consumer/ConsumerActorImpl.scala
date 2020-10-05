package tech.canaryapp.server.auth.actor.consumer

import akka.Done
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.kafka.{CommitterSettings, Subscriptions}
import akka.kafka.scaladsl.{Committer, Consumer, Producer}
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.stream.{Graph, OverflowStrategy}
import akka.stream.scaladsl.{Flow, GraphDSL, Keep, Sink}
import com.typesafe.scalalogging.LazyLogging
import monix.execution.Scheduler
import org.apache.kafka.clients.consumer.{ConsumerConfig => KafkaConsumerConfig}
import org.apache.kafka.clients.producer.{Producer => KafkaProducer}
import tech.canaryapp.server.auth.actor.consumer.ConsumerActor.{Message, Stop}
import tech.canaryapp.server.auth.config.AuthServiceConfig

import scala.concurrent.ExecutionContextExecutor
import scala.language.postfixOps
import scala.util.{Failure, Success}

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object ConsumerActorImpl extends LazyLogging {

  def createActor(
    config: AuthServiceConfig,
    producer: KafkaProducer[Array[Byte], Array[Byte]],
    committerSettings: CommitterSettings,
    scheduler: Scheduler
  ): ConsumerActor.Provider =
    (forwarderActor) =>
      Behaviors.setup[Message] { context =>
        lazy val systemConfig = config.systemConfig

        implicit val sys: ActorSystem[Nothing] = context.system
        implicit val exe: ExecutionContextExecutor = context.executionContext

//        lazy val consumerFlow = Flow.fromGraph(GraphDSL.create() { implicit builder =>
//          import GraphDSL.Implicits._
//
//
//        })



        val drainingControl = Consumer
          .committableSource(
            config.kafkaConsumerSettings
              .withGroupId(s"${systemConfig.clusterName.name}-auth-consumer")
              .withProperty(KafkaConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
              .withProperty(KafkaConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"),
            Subscriptions.topics(config.systemConfig.kafkaConfig.topics: _*)
          )
          .buffer(config.bufferSize, OverflowStrategy.backpressure)
          .idleTimeout(config.idleTimeout)
          .groupedWithin(config.maxBatchSize, config.maxBatchAge)
          .toMat(Sink.ignore)(Keep.both)
          .mapMaterializedValue(DrainingControl.apply[Done])
          .run()


        drainingControl.isShutdown.onComplete {
          case Failure(exception) =>
            logger.error("Canary Auth service consumer stream failed.", exception)
            context.self ! Stop
          case Success(_) =>
            logger.info("Canary Auth service consumer stream finished.")
            context.self ! Stop
        }

        Behaviors.receiveMessage {
          case Stop =>
            context.pipeToSelf(drainingControl.drainAndShutdown()) {
              case Failure(exception) =>
                Stop
              case Success(value) =>
                Stop
            }

            Behaviors.receiveMessage {
              case ConsumerActor.Stop =>
                Behaviors.stopped
            }
        }

      }

}
