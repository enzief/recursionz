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

import scalaz.tc.EqSyntax

import enzief.recursionz.example.Peano
import enzief.recursionz.example.Peano._
import enzief.recursionz.example.PeanoF.eqp
import enzief.recursionz.syntax._

import testz._

object PeanoTest extends EqSyntax {

  def tests[A](harness: Harness[A]): A = {
    import harness._

    val three = succ(succ(succ(zero)))

    section(
      test("cata into integer") { () =>
        assert(three.cata(count) === 3)
      },
      test("ana from integer") { () =>
        assert(4.ana[Peano](uncount) === succ(three))
      },
      test("cata zero") { () =>
        assert(zero.cata(count) === 0)
      },
      test("ana zero") { () =>
        assert(0.ana[Peano](uncount) === zero)
      }
    )
  }
}
