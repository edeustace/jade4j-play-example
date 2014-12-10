name := """jade4j-play-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"


resolvers ++= Seq("jade4j" at "https://raw.github.com/neuland/jade4j/master/releases")

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "de.neuland-bfi" % "jade4j" % "0.4.2"
)
