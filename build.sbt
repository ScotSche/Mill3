val scala3Version = "3.0.0-M3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-simple",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.3",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.3" % "test"
  )
