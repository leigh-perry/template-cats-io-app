package com.lptemplatecompany.lptemplatedivision.shared
package interpreter

import cats.Monad
import cats.instances.list._
import cats.instances.order._
import cats.instances.string._
import cats.syntax.applicative._
import cats.syntax.functor._
import cats.syntax.traverse._
import com.lptemplatecompany.lptemplatedivision.lptemplateservicename.BuildInfo
import com.lptemplatecompany.lptemplatedivision.shared.algebra.InfoAlg

/**
  * The real-infrastructure implementation for logging of application information, typically at
  * application startup
  *
  * C = config class
  */
class Info[F[_] : Monad, C](cfg: C, log: String => F[Unit])
  extends InfoAlg[F] {

  import scala.collection.JavaConverters._

  override def systemProperties: F[Map[String, String]] =
    System.getProperties.asScala.toMap.pure[F]

  override def environmentVariables: F[Map[String, String]] =
    System.getenv.asScala.toMap.pure[F]

  override def logBanner: F[Unit] =
    log(banner)

  override def logMap(m: Map[String, String]): F[Unit] =
    m.toList
      .sortBy(_._1)
      .traverse(e => log(formatMapEntry(e)))
      .void

  override def logConfig: F[Unit] =
    log(s"Configuration $cfg")

  override def logSeparator: F[Unit] =
    log(separator)

  override def logTitle(title: String): F[Unit] =
    log(title)

  private val banner =
    s"""${Apps.className(this)} process version: ${BuildInfo.version}
       |  scala-version         : ${BuildInfo.scalaVersion}
       |  sbt-version           : ${BuildInfo.sbtVersion}
       |  build-time            : ${BuildInfo.buildTime}
       |  git-commit            : ${BuildInfo.gitCommitIdentifier}
       |  gitCommitIdentifier   : ${BuildInfo.gitCommitIdentifier}
       |  gitHashShort          : ${BuildInfo.gitHashShort}
       |  gitBranch             : ${BuildInfo.gitBranch}
       |  gitCommitAuthor       : ${BuildInfo.gitCommitAuthor.replaceAll("/", " - ")}
       |  gitCommitDate         : ${BuildInfo.gitCommitDate}
       |  gitMessage            : ${Apps.loggable(BuildInfo.gitMessage.trim)}
       |  gitUncommittedChanges : ${BuildInfo.gitUncommittedChanges}
       |  library-dependencies  : ${BuildInfo.libraryDependencies}"""
      .stripMargin

  private val separator = "================================================================================"

  private def formatMapEntry(e: (String, String)): String =
    s"${e._1}=${Apps.loggable(e._2)}"
}

object Info {
  def of[F[_] : Monad, C](cfg: C, log: String => F[Unit]): F[Info[F, C]] =
    new Info(cfg, log)
      .pure[F]
}
