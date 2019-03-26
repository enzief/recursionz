// Copyright (c) 2019 Yui Pham.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// "distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package enzief.recursionz

package test

import scala.Console._
import scala.{Array, Exception, List, Nil, StringContext}
import scala.Predef.ArrowAssoc
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration.Duration

import scalaz.Predef._

import testz._
import testz.runner._

object Main {

  def printReport(scope: List[String], out: Result): List[String] =
    (out match {
      case _: Succeed => s"\n[${green("OK")}]" :: green(" > ") :: scope
      case _: Fail    => s"\n[${red("KO")}]" :: red(" > ") :: scope
    })

  def green(s: String): String = s"$GREEN$s$RESET"
  def red(s:   String): String = s"$RED$s$RESET"

  val harness: Harness[PureHarness.Uses[Unit]] =
    PureHarness.makeFromPrinter(
      (ls, tr) => runner.printStrs(printReport(tr, ls), print)
    )

  def suites(harness: Harness[PureHarness.Uses[Unit]]): List[() => Future[TestOutput]] =
    tests(harness).map {
      case (name, suite) =>
        () => Future.successful(suite((), name :: Nil))
    }

  def tests[T](harness: Harness[T]): List[(String, T)] =
    "Peano"   -> PeanoTest.tests(harness) ::
    "RawData" -> RawDataTest.tests(harness) ::
    Nil

  def main(args: Array[String]): Unit = {
    val result = Await.result(runner(suites(harness), print, global), Duration.Inf)
    if (result.failed) throw /* scalafix:ok */ new Exception /* scalafix:ok */ ("some tests failed")
  }
}
