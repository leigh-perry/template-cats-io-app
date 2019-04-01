package com.lptemplatecompany.lptemplatedivision.lptemplateservicename
package config

import cats.effect.{IO, Resource}

/**
  * Top level application resources held in a Resource[...] so that proper cleanup happens
  * on program termination, whether clean or failure.
  */
final case class Context[F[_]] private(
  // TODO
)

object Context {
  def create: Resource[IO, Context[IO]] =
    Resource.liftF(IO(new Context[IO]()))
}
