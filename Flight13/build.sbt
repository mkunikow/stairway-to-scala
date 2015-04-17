name := "Flight13"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.2"

scalacOptions += "-deprecation"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

libraryDependencies += "org.scala-lang" % "scala-actors" % "2.11.2"

libraryDependencies += "junit" % "junit" % "4.5" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.4"

libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.3.4"
