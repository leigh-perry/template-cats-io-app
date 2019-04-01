package com.lptemplatecompany.lptemplatedivision.lptemplateservicename

import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.apply._
import com.lptemplatecompany.lptemplatedivision.lptemplateservicename.config.{Config, Context}
import com.lptemplatecompany.lptemplatedivision.shared.interpreter.Info

/**
  * All resources, such as temporary directories and the expanded files, are cleaned up when no longer
  * required. This is implemented using `cats.effect.Resource`.
  */
object AppMain
  extends IOApp {

  private def getLogger: IO[String => IO[Unit]] =
    IO(s => IO(println(s)))

  override def run(args: List[String]): IO[ExitCode] =
    for {
      outcome <- program.attempt
      log <- getLogger
      code <- outcome.fold(
        e => log(s"Application failed: $e") *> IO(ExitCode.Error),
        _ => log("Application terminated with no error indication") *> IO(ExitCode.Success)
      )
    } yield code

  private def program: IO[Unit] =
    for {
      log <- getLogger
      cfg <- Config.load
      info <- Info.of[IO, Config](cfg, log)
      _ <- info.logEnvironment
      _ <- log(cfg.toString)
      outcome <- runApp
    } yield outcome

  import scala.concurrent.duration._

  private def runApp: IO[Unit] =
    Context.create
      .use {
        _ =>
          IO.sleep(1.seconds)
      }

}
