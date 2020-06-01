ThisBuild / scalaVersion := "2.13.2"
ThisBuild / organization := "tech.canaryapp"

scalacOptions in ThisBuild ++= Seq(
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

val akkaVersion = "2.6.5"
val akkaHttpVersion = "10.1.12"
val macwireVersion = "2.3.3"
val circeVersion = "0.13.0"
val monixVersion = "3.1.0"
val logbackClassicVersion = "1.2.3"
val catsVersion = "2.1.1"
val scalatestVersion = "3.1.1"
val pureconfigVersion = "0.12.3"
val akkaHttpCirce = "1.32.0"

val commonSettings = Seq(
  organization := "tech.canaryapp",
  scalaVersion := "2.13.2",
  developers := List(
    Developer(
      id = "tmihalicka",
      name = "Tomas Mihalicka",
      email = "tomas@mihalicka.com",
      url = url("http://www.mihalicka.com")
    )
  ),
  //  wartremoverErrors ++= Warts.all
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "viewer-api",
    libraryDependencies ++= Seq(
      // Akka Core
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-slf4j"               % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      // Akka Others
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      // MacWire
      "com.softwaremill.macwire" %% "macros"     % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "macrosakka" % macwireVersion % Provided,
      "com.softwaremill.macwire" %% "util"       % macwireVersion,
      "com.softwaremill.macwire" %% "proxy"      % macwireVersion,
      // Circe
      "io.circe" %% "circe-core"    % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser"  % circeVersion,
      "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirce,
      // Monix
      "io.monix" %% "monix-eval"      % monixVersion,
      "io.monix" %% "monix-execution" % monixVersion,
      // Others
      "ch.qos.logback"        % "logback-classic"           % logbackClassicVersion,
      "org.typelevel"         %% "cats-core"                % catsVersion,
      "org.scalatest"         %% "scalatest"                % scalatestVersion % Test,
      "com.github.pureconfig" %% "pureconfig"               % pureconfigVersion,
      // Crypto Utils
      "org.whispersystems" % "curve25519-java"              % "0.5.0"
    )
  )


