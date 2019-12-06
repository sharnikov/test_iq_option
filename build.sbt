lazy val commonSettings = Seq(
  name := "Data Fetcher",
  organization := "test.option.iq",
  scalaVersion := "2.11.12",
  version := "0.1"
)


lazy val assamblySettings = Seq(
  mainClass in assembly := Some("test.option.iq.Fetcher"),
  assemblyMergeStrategy in assembly := {
    case PathList("org.slf4j", "impl", xs @ _*) => MergeStrategy.first
    case x if x.contains("io.netty.versions.properties") => MergeStrategy.discard
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    commonSettings,
    assamblySettings,
    resourceDirectory := baseDirectory.value / "resources",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.client" %% "async-http-client-backend-future" % "2.0.0-RC4",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.7",
      "com.typesafe.akka" %% "akka-stream" % "2.5.20",
      "io.spray" %% "spray-json" % "1.3.5"
    )
  )

