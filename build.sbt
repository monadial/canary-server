ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.monadial"
ThisBuild / organizationName := "Monadial"

ThisBuild / licenses += "AGPLv3" -> url("https://www.gnu.org/licenses/agpl-3.0.en.html")
ThisBuild / startYear := Some(2021)

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint:adapted-args,inaccessible",
  "-Wvalue-discard",
  "-Wdead-code",
  "-Yrangepos",
  "-Ywarn-unused:imports"
)

dependencyUpdatesFilter -= moduleFilter(organization = "org.scala-lang")

val typesafeConfig = "1.4.1"
val akkaVersion = "2.6.16"
val akkaHttpVersion = "10.2.6"
val akkaStreamKafkaVersion = "2.1.1"
val macwireVersion = "2.4.1"
val circeVersion = "0.13.0"
val monixVersion = "3.4.0"
val logbackClassicVersion = "1.2.6"
val catsVersion = "2.1.1"
val scalatestVersion = "3.2.0"
val pureconfigVersion = "0.13.0"
val akkaHttpCirce = "1.33.0"
val doobieVersion = "0.13.4"
val flywayVersion = "7.15.0"
val kamonVersion = "2.2.3"
val bouncyCastleVersion = "1.69"

val commonSettings = Seq(
  organization := "tech.canaryapp",
  scalaVersion := "2.13.6",
  developers := List(
    Developer(
      id = "tmihalicka",
      name = "Tomas Mihalicka",
      email = "tomas@mihalicka.com",
      url = url("https://www.mihalicka.com")
    )
  ),
  assembly / test := {},
  assembly / assemblyJarName := "assembly.jar",
  assembly / assemblyMergeStrategy := {
    case x if x.endsWith(".conf") => MergeStrategy.concat
    case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
    case x if x.endsWith("module-info.class") => MergeStrategy.first
    case x if x.endsWith("LICENSE") => MergeStrategy.discard
    case x if x.endsWith("NOTICE") => MergeStrategy.discard
    case x if x.endsWith(".txt") => MergeStrategy.discard
    case PathList("META-INF", _@_*) => MergeStrategy.discard
    case _ => MergeStrategy.deduplicate
  }
  //  wartremoverErrors ++= Warts.all
)


lazy val root = (project in file("."))
  .aggregate(commonModel, commonService, commonUtil)
  .aggregate(serviceAuth, serviceCrypto, serviceRing)
  .settings(commonSettings: _*)
  .settings(name := "canary", publish / skip := true)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.monadial.canary.server.model"
  )

lazy val commonUtil = (project in file("common-util"))
  .settings(
    name := "common-util",
    libraryDependencies ++= Seq(
      // Akka
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-kafka" % akkaStreamKafkaVersion,
      // Akka Others
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      // Monix
      "io.monix" %% "monix-eval" % monixVersion,
      "io.monix" %% "monix-execution" % monixVersion,
      // Others
      "com.typesafe" % "config" % typesafeConfig,
      // Others
      "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
      "com.typesafe" % "config" % typesafeConfig,
    )
  )

lazy val commonModel = (project in file("common-model"))
  .dependsOn(commonUtil)
  .settings(commonSettings: _*)
  .settings(
    name := "common-model"
  )

lazy val commonService = (project in file("common-service"))
  .dependsOn(commonUtil)
  .settings(commonSettings: _*)
  .settings(
    name := "common-service",
    libraryDependencies ++= Seq(
      "io.kamon" %% "kamon-bundle" % kamonVersion,
      // Cryptography
      "org.bouncycastle"  % "bcprov-jdk15on" % bouncyCastleVersion
    )
  )

lazy val serviceCrypto = (project in file("service-crypto"))
  .dependsOn(commonService, commonModel, commonUtil)
  .settings(commonSettings: _*)
  .settings(
    name := "service-crypto",
    libraryDependencies ++= Seq(
      // Akka
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      // MacWire
      "com.softwaremill.macwire" %% "macros" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "util" % macwireVersion,
      "com.softwaremill.macwire" %% "proxy" % macwireVersion,
      // Monix
      "io.monix" %% "monix-eval" % monixVersion,
      "io.monix" %% "monix-execution" % monixVersion,
      // Kamon
      "io.kamon" %% "kamon-bundle" % kamonVersion
    ),
    assembly / mainClass := Some("com.monadial.canary.server.crypto.CryptoService")
  )


lazy val serviceAuth = (project in file("service-auth"))
  .dependsOn(commonService, commonModel, commonUtil)
  .settings(commonSettings: _*)
  .settings(
    name := "service-auth",
    libraryDependencies ++= Seq(
      // Akka
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-kafka" % akkaStreamKafkaVersion,
      // Akka Others
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      // MacWire
      "com.softwaremill.macwire" %% "macros" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "util" % macwireVersion,
      "com.softwaremill.macwire" %% "proxy" % macwireVersion,
      // Monix
      "io.monix" %% "monix-eval" % monixVersion,
      "io.monix" %% "monix-execution" % monixVersion,
      // Doboie
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.flywaydb" % "flyway-core" % flywayVersion,
      // Kamon
      "io.kamon" %% "kamon-bundle" % kamonVersion
    ),
    assembly / mainClass := Some("com.monadial.canary.server.auth.AuthService")
  )

// responsible for management of ring of trust
lazy val serviceRing = (project in file("service-ring"))
  .dependsOn(commonService, commonModel, commonUtil)
  .settings(commonSettings: _*)
  .settings(
    name := "service-ring",
    libraryDependencies ++= Seq(
      // Akka
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      // MacWire
      "com.softwaremill.macwire" %% "macros" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "util" % macwireVersion,
      "com.softwaremill.macwire" %% "proxy" % macwireVersion,
      // Monix
      "io.monix" %% "monix-eval" % monixVersion,
      "io.monix" %% "monix-execution" % monixVersion,
      // Kamon
      "io.kamon" %% "kamon-bundle" % kamonVersion
    ),
    assembly / mainClass := Some("com.monadial.canary.server.ring.RingService")
  )

// responsible for sending data back to client
lazy val serviceChannel = (project in file("service-channel"))
  .dependsOn(commonService, commonModel, commonUtil)
  .settings(commonSettings: _*)
  .settings(
    name := "service-channel",
    libraryDependencies ++= Seq(
      // Akka
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      // MacWire
      "com.softwaremill.macwire" %% "macros" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "util" % macwireVersion,
      "com.softwaremill.macwire" %% "proxy" % macwireVersion,
      // Monix
      "io.monix" %% "monix-eval" % monixVersion,
      "io.monix" %% "monix-execution" % monixVersion,
      // Kamon
      "io.kamon" %% "kamon-bundle" % kamonVersion
    ),
    assembly / mainClass := Some("com.monadial.canary.server.channel.ChannelService")
  )

lazy val serviceNotification = (project in file("service-notification"))
  .dependsOn(commonService, commonModel, commonUtil)
  .settings(commonSettings: _*)
  .settings(
    name := "service-notification",
    libraryDependencies ++= Seq(
      // Akka
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      // MacWire
      "com.softwaremill.macwire" %% "macros" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "util" % macwireVersion,
      "com.softwaremill.macwire" %% "proxy" % macwireVersion,
      // Monix
      "io.monix" %% "monix-eval" % monixVersion,
      "io.monix" %% "monix-execution" % monixVersion,
      // Kamon
      "io.kamon" %% "kamon-bundle" % kamonVersion
    ),
    assembly / mainClass := Some("com.monadial.canary.server.notification.NotificationService")
  )

lazy val serviceSms = (project in file("service-sms"))
  .dependsOn(commonService, commonModel, commonUtil)
  .settings(commonSettings: _*)
  .settings(
    name := "service-sms",
    libraryDependencies ++= Seq(
      // Akka
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      // MacWire
      "com.softwaremill.macwire" %% "macros" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "util" % macwireVersion,
      "com.softwaremill.macwire" %% "proxy" % macwireVersion,
      // Monix
      "io.monix" %% "monix-eval" % monixVersion,
      "io.monix" %% "monix-execution" % monixVersion,
      // Kamon
      "io.kamon" %% "kamon-bundle" % kamonVersion
    ),
    assembly / mainClass:= Some("com.monadial.canary.server.sms.SmsService")
  )

lazy val serviceEmail = (project in file("service-email"))
  .dependsOn(commonService, commonModel, commonUtil)
  .settings(commonSettings: _*)
  .settings(
    name := "service-email",
    libraryDependencies ++= Seq(
      // Akka
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      // MacWire
      "com.softwaremill.macwire" %% "macros" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "util" % macwireVersion,
      "com.softwaremill.macwire" %% "proxy" % macwireVersion,
      // Monix
      "io.monix" %% "monix-eval" % monixVersion,
      "io.monix" %% "monix-execution" % monixVersion,
      // Kamon
      "io.kamon" %% "kamon-bundle" % kamonVersion
    ),
    assembly / mainClass := Some("com.monadial.canary.server.email.EmailService")
  )

lazy val serviceNone = (project in file("service-nonce"))
  .dependsOn(commonService, commonModel, commonUtil)
  .settings(commonSettings: _*)
  .settings(
    name := "service-nonce",
    libraryDependencies ++= Seq(
      // Akka
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      // MacWire
      "com.softwaremill.macwire" %% "macros" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "util" % macwireVersion,
      "com.softwaremill.macwire" %% "proxy" % macwireVersion,
      // Monix
      "io.monix" %% "monix-eval" % monixVersion,
      "io.monix" %% "monix-execution" % monixVersion,
      // Kamon
      "io.kamon" %% "kamon-bundle" % kamonVersion
    ),
    assembly / mainClass := Some("com.monadial.canary.server.email.EmailService")
  )
