import sbt._

object Dependencies {
  object Version {
    val kindProjectorVersion = "0.10.3"

    val cats = "2.3.1"
    val catsEffect = "2.3.1"
    val catsMouse = "0.26.2"

    val logback = "1.2.3"
    val log4cats = "1.1.1"
    val conduction = "0.6.2"

    val minitest = "2.9.2"
    val scalacheck = "1.15.2"
  }

  val cats = "org.typelevel" %% "cats-core" % Version.cats
  val catsEffect = "org.typelevel" %% "cats-effect" % Version.catsEffect
  val catsMouse = "org.typelevel" %% "mouse" % Version.catsMouse

  val logback = "ch.qos.logback" % "logback-classic" % Version.logback
  val log4catsSlf4j = "io.chrisdavenport" %% "log4cats-slf4j" % Version.log4cats
  val conduction = "com.github.leigh-perry" %% "conduction-core" % Version.conduction
  val conductionMagnolia = "com.github.leigh-perry" %% "conduction-magnolia" % Version.conduction

  val minitest = "io.monix" %% "minitest" % Version.minitest
  val minitestLaws = "io.monix" %% "minitest-laws" % Version.minitest
  val scalacheck = "org.scalacheck" %% "scalacheck" % Version.scalacheck
  val catsLaws = "org.typelevel" %% "cats-laws" % Version.cats
}
