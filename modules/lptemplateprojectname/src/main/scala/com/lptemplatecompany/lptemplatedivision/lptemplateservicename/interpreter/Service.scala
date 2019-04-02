package com.lptemplatecompany.lptemplatedivision.lptemplateservicename.interpreter

import cats.effect.{IO, Resource, Timer}
import com.lptemplatecompany.lptemplatedivision.lptemplateservicename.algebra.ServiceAlg
import com.lptemplatecompany.lptemplatedivision.lptemplateservicename.config.Config
import io.chrisdavenport.log4cats.Logger

/**
  * The real-infrastructure implementation for the top level service
  */
class Service private(cfg: Config, log: Logger[IO])
  extends ServiceAlg[IO] {

  implicit val timer: Timer[IO] = IO.timer(scala.concurrent.ExecutionContext.global)

  import scala.concurrent.duration._

  override def run: IO[Unit] =
    IO.sleep(2.seconds)

}

object Service {
  def resource(cfg: Config, log: Logger[IO]): Resource[IO, ServiceAlg[IO]] =
    Resource.liftF(IO(new Service(cfg, log): ServiceAlg[IO]))
}

