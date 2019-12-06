lazy val settings = Seq(
  name := "Data Fetcher",
  organization := "Test",
  scalaVersion := "2.11.12",
  version := "0.1",
  mainClass in assembly := Some("test.option.iq.Fetcher")
)

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    settings,
    resourceDirectory := baseDirectory.value / "resources",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.client" %% "async-http-client-backend-future" % "2.0.0-RC4",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.7",
      "com.typesafe.akka" %% "akka-stream" % "2.5.19",
      "io.spray" %% "spray-json" % "1.3.5"
    )
  )