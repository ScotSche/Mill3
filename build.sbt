val scala3Version = "3.0.0-M3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-simple",
    version := "0.1.0",

    scalaVersion := scala3Version,

    //resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases",
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",
    libraryDependencies += ("org.scalactic" %% "scalactic" % "3.2.2").withDottyCompat(scalaVersion.value),
    libraryDependencies += ("org.scalatest" %% "scalatest" % "3.2.2" % "test").withDottyCompat(scalaVersion.value)

  )
