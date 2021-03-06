package com.lptemplatecompany.lptemplatedivision.lptemplateservicename
package config

import cats.effect.{ IO, Resource }
import com.lptemplatecompany.lptemplatedivision.lptemplateservicename.algebra.ServiceAlg
import com.lptemplatecompany.lptemplatedivision.lptemplateservicename.interpreter.Service
import io.chrisdavenport.log4cats.Logger

/**
 * Top level application resources held in a Resource[...] so that proper cleanup happens
 * on program termination, whether clean or failure.
 */
final case class Context[F[_]] private (
  service: ServiceAlg[F]
)

object Context {
  def create(cfg: AppConfig, log: Logger[IO]): Resource[IO, Context[IO]] =
    for {
      service <- Service.resource(cfg, log)
    } yield new Context[IO](service)
}
