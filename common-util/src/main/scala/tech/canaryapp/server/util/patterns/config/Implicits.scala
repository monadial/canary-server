package tech.canaryapp.server.util.patterns.config

import com.typesafe.config.Config

import java.util.{List => JList}
import scala.jdk.CollectionConverters._

/**
 * @author Tomas Mihalicka <tomas@mihalicka.com>
 */
object Implicits {

  implicit class ConfigOps(val underlying: Config) extends AnyVal {

    // scalar
    def getStringOption(path: String): Option[String] = getValue[String](path)

    def getIntOption(path: String): Option[Int] = getValue[Int](path)

    def getBooleanOption(path: String): Option[Boolean] = getValue[Boolean](path)

    def getNumberOption(path: String): Option[Number] = getValue[Number](path)

    // list
    def getStringListOption(path: String): Option[List[String]] = getList[String](path)

    def getIntListOption(path: String): Option[List[Int]] = getList[Int](path)

    def getBooleanListOption(path: String): Option[List[Boolean]] = getList[Boolean](path)

    def getNumberListOption(path: String): Option[List[Number]] = getList[Number](path)

    private def getValue[T](path: String): Option[T] = if (underlying.hasPath(path)) {
      Some(underlying
        .getAnyRef(path)
        .asInstanceOf[T])
    } else {
      None
    }

    private def getList[T](path: String): Option[List[T]] = if (underlying.hasPath(path)) {
      Option(underlying
        .getAnyRefList(path)
        .asInstanceOf[JList[T]]
        .asScala
        .toList
      )
    } else {
      None
    }
  }

}
