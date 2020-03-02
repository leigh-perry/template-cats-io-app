import sbt._

object Dependencies {
  object Version {
    val kindProjectorVersion = "0.10.3"

    val cats = "2.1.1"
    val catsEffect = "2.1.2"

    val logback = "1.2.3"
    val log4cats = "1.0.1"
    val conduction = "0.4.2"

    val minitest = "2.7.0"
    val scalacheck = "1.14.3"
  }

  val cats = "org.typelevel" %% "cats-core" % Version.cats
  val catsEffect = "org.typelevel" %% "cats-effect" % Version.catsEffect

  val logback = "ch.qos.logback" % "logback-classic" % Version.logback
  val log4catsSlf4j = "io.chrisdavenport" %% "log4cats-slf4j" % Version.log4cats
  val conduction = "com.github.leigh-perry" %% "conduction-core" % Version.conduction

  val minitest = "io.monix" %% "minitest" % Version.minitest
  val minitestLaws = "io.monix" %% "minitest-laws" % Version.minitest
  val scalacheck = "org.scalacheck" %% "scalacheck" % Version.scalacheck
  val catsLaws = "org.typelevel" %% "cats-laws" % Version.cats
}
