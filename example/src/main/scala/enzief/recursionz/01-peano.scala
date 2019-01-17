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

import scalaz.Scalaz._
import scalaz.data.Maybe
import scalaz.data.MaybeModule._

package object example {
  type PeanoF[A] = Maybe[A]
  type Peano     = Fix[PeanoF]
}

package example {

  object Peano {
    val zero: Peano = Fix(Maybe.empty)

    def succ(p: Peano): Peano = Fix(Maybe.just(p))

    def toInt(p: Peano): Int =
      Recursive.fromT[Fix, PeanoF].cata(p)(count)

    val count: Algebra[PeanoF, Int] = {
      case Maybe.Just(i) => 1 + i
      case Maybe.Empty() => 0
    }
  }
}
