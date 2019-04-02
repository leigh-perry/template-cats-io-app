package com.lptemplatecompany.lptemplatedivision.lptemplateservicename.syntax

import cats.syntax.either._
import com.lptemplatecompany.lptemplatedivision.lptemplateservicename.AppError
import com.lptemplatecompany.lptemplatedivision.shared.testsupport.TestSupport
import minitest.SimpleTestSuite
import minitest.laws.Checkers

object IOSyntaxTest
  extends SimpleTestSuite
    with IOSyntax
    with Checkers
    with TestSupport {

  test("exception catching") {
    check1 {
      v: String =>
        ((throw new RuntimeException(v)): Int).failWith(s"message $v")
          .attempt
          .unsafeRunSync()
          .shouldSatisfy {
            case Left(AppError.ExceptionEncountered(_)) => true
            case _ => false
          }
    }
  }

  test("success path") {
    check1 {
      v: Int =>
        v.failWith(s"message $v")
          .attempt
          .unsafeRunSync()
          .shouldBe(v.asRight)
    }
  }
}
