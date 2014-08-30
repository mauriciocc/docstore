import play.PlayScala

name := """docstore"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,  
  filters,
  "org.webjars" % "bootstrap" % "3.2.0",
  "org.webjars" % "font-awesome" % "4.2.0",
  "org.webjars" % "jquery" % "2.1.1",
  "org.webjars" % "angularjs" % "1.3.0-beta.18",
  "org.webjars" % "angular-translate" % "2.2.0"
)