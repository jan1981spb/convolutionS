ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

lazy val root = (project in file("."))
  .settings(
    name := "scalaconvolution"
  )

libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.5.9" % "provided"
libraryDependencies += "org.junit.jupiter" % "junit-jupiter-api" % "5.2.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % Test
