import play.PlayScala

name := """docstore"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,  
  filters,
  anorm,
  "com.google.guava" % "guava" % "18.0",
  "com.google.inject" % "guice" % "4.0-beta4",
  "org.webjars" % "bootstrap" % "3.2.0",
  "org.webjars" % "font-awesome" % "4.2.0",
  "org.webjars" % "ionicons" % "1.5.2",
  "org.webjars" % "jquery" % "2.1.1",
  "org.webjars" % "angularjs" % "1.3.0-beta.18",
  "org.webjars" % "angular-translate" % "2.2.0"
)