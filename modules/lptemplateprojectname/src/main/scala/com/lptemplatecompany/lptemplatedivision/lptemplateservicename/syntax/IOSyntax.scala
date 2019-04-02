package com.lptemplatecompany.lptemplatedivision.lptemplateservicename.syntax

import cats.effect.IO
import cats.syntax.either._
import com.lptemplatecompany.lptemplatedivision.lptemplateservicename.AppError

final class IOSyntaxSafeOps[A](a: => A) {
  def failWith(message: String): IO[A] =
    IO.delay(a)
      .attempt
      .flatMap(e => IO.fromEither(e.leftMap(t => AppError.exception(message, t))))
}

trait ToIOSyntaxSafeOps {
  implicit def ops[A](a: => A): IOSyntaxSafeOps[A] =
    new IOSyntaxSafeOps[A](a)
}

////

trait IOSyntax
  extends ToIOSyntaxSafeOps

object aiosyntaxinstances
  extends IOSyntax
