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

import scala.Nil
import scala.Option

import scalaz.Predef._
import scalaz.laws._
import scalaz.tc.EqSyntax

import enzief.recursionz.example.RawData
import enzief.recursionz.example.RawList
import enzief.recursionz.example.RawInt

import testz._

object RawDataTest extends EqSyntax {

  def tests[A](harness: Harness[A]): A = {
    import harness._

    namedSection("forms a traversable")(
      test("composition law") { () =>
        TraversableLaws.traverseComposition(RawInt(100): RawData[Int])(
          i => Option(i.toString),
          (_: String) => () :: Nil
        ) { (x, y) =>
          assert(x === y)
        }
      },
      test("idetity law") { () =>
        TraversableLaws.traverseIdentity(
          RawList("123" :: "dsf" :: Nil): RawData[String]
        ) { (x, y) =>
          scala.Predef.println(x)
          scala.Predef.println(y)
          assert(x === y)
        }
      }
    )
  }
}
