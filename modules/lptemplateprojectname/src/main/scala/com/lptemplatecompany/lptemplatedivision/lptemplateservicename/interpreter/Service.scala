package com.lptemplatecompany.lptemplatedivision.lptemplateservicename.interpreter

import cats.effect.{IO, Resource, Timer}
import cats.syntax.apply._
import com.lptemplatecompany.lptemplatedivision.lptemplateservicename.AppError
import com.lptemplatecompany.lptemplatedivision.lptemplateservicename.algebra.ServiceAlg
import com.lptemplatecompany.lptemplatedivision.lptemplateservicename.config.Config
import com.lptemplatecompany.lptemplatedivision.lptemplateservicename.syntax.IOSyntax
import io.chrisdavenport.log4cats.Logger

/**
  * The real-infrastructure implementation for the top level service
  */
class Service private(cfg: Config, log: Logger[IO], tempDir: String)
  extends ServiceAlg[IO] {

  implicit val timer: Timer[IO] = IO.timer(scala.concurrent.ExecutionContext.global)

  import scala.concurrent.duration._

  override def run: IO[Unit] =
    log.info(s"Starting in $tempDir") *>
      IO.sleep(10.seconds) <*
      log.info(s"Finishing in $tempDir")

}

object Service {
  def resource(cfg: Config, log: Logger[IO]): Resource[IO, ServiceAlg[IO]] =
    for {
      tempDir <- FileSystem.tempDirectoryScope(log)
      svc <- Resource.liftF(IO(new Service(cfg, log, tempDir): ServiceAlg[IO]))
    } yield svc
}

//// sample code only

import java.io.File
import java.nio.file.{Files, Path}
import java.util.UUID

import cats.syntax.functor._
import cats.syntax.monadError._

object FileSystem
  extends IOSyntax {

  def tempDirectoryScope(log: Logger[IO]): Resource[IO, String] =
    Resource.make {
      for {
        file <- FileSystem.createTempDir
        _ <- log.info(s"Created temp directory $file")
      } yield file
    } {
      dir =>
        FileSystem.deleteFileOrDirectory(dir) *>
          log.info(s"Removed temp directory $dir")
    }

  def tempFilename(extension: Option[String]): IO[String] =
    UUID.randomUUID.toString
      .failWithMsg("UUID.randomUUID failed")
      .map(name => extension.fold(name)(ext => s"$name.$ext"))

  def baseTempDir: IO[String] =
    System.getProperty("java.io.tmpdir")
      .failWithMsg("Could not get tmpdir")

  def deleteFileOrDirectory(filepath: String): IO[Unit] =
    delete(new File(filepath))
      .failWithMsg(s"Could not delete $filepath")
      .ensure(AppError.DirectoryDeleteFailed(filepath))(identity)
      .void

  def createTempDir[A]: IO[String] =
    for {
      base <- baseTempDir
      parent <- IO(Path.of(base))
      tempPath <- Files.createTempDirectory(parent, "workspace").failWithMsg("Could not create temp dir")
      tempDir <- tempPath.toFile.getAbsolutePath.failWithMsg(s"Could not resolve $tempPath")
    } yield tempDir

  //// internal impure code

  private def delete(file: File): Boolean = {
    if (file.isDirectory) {
      // Delete the contents of the directory first
      val children = file.list
      for (element <- children) {
        delete(new File(file, element))
      }
    }
    file.delete
  }

}