name := """snooker-o"""

version := "1.0-SNAPSHOT"

maintainer := "ian.welsh@company.org"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies += ws
libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.6"
libraryDependencies ++= Seq(
    "com.beachape" %% "enumeratum" % "1.5.13"
)

libraryDependencies += specs2 % Test

scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-Xfatal-warnings"
)
