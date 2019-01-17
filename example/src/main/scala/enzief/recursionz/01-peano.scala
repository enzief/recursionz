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

package example

import scalaz.Predef.Int
import scalaz.Scalaz._
import scalaz.tc._

sealed trait PeanoF[A]
final case class SuccF[A](a: A) extends PeanoF[A]
final case class ZeroF[A]() extends PeanoF[A]

object PeanoF {

  implicit val functor: Functor[PeanoF] = instanceOf {
    new FunctorClass[PeanoF] {
      def map[A, B](ma: PeanoF[A])(f: A => B): PeanoF[B] =
        ma match {
          case SuccF(a) => SuccF(f(a))
          case ZeroF()  => ZeroF()
        }
    }
  }
}

object Peano {
  type Peano = Fix[PeanoF]

  val zero: Peano = Fix(ZeroF())

  def succ(p: Peano): Peano = Fix(SuccF(p))

  def toInt(p: Peano): Int =
    Recursive[PeanoF, Peano].cata(p)(count)

  val count: Algebra[PeanoF, Int] = {
    case SuccF(i) => 1 + i
    case ZeroF()  => 0
  }
}
