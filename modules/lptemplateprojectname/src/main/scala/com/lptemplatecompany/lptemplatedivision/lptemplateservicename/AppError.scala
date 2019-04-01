package com.lptemplatecompany.lptemplatedivision.lptemplateservicename

import cats.Show
import cats.data.NonEmptyChain
import com.lptemplatecompany.lptemplatedivision.shared.Apps
import com.lptemplatecompany.lptemplatedivision.shared.config.ConfiguredError

sealed trait AppError extends Throwable {
  override def toString: String =
    Show[AppError].show(this)
}

/**
  * The ADT of error types for the application. IO requires a Throwable subclass.
  */
object AppError {
  final case class InvalidConfiguration(errors: NonEmptyChain[ConfiguredError]) extends AppError
  final case class ExceptionEncountered(message: String) extends AppError

  def exception(message: String, e: Throwable): AppError =
    ExceptionEncountered(s"Exception $message: ${Apps.stackTrace(e)}")

  implicit val showAppError: Show[AppError] =
    (t: AppError) => {
      val extra: String =
        t match {
          case InvalidConfiguration(errors) => errors.toString
          case ExceptionEncountered(message) => message.toString
        }
      s"${Apps.className(t)}: $extra"
    }
}
