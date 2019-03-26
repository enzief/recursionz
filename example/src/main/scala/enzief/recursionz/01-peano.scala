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

import enzief.recursionz.syntax._
import enzief.recursionz.typeclass._

sealed trait PeanoF[A]
final case class SuccF[A](a: A) extends PeanoF[A]
final case class ZeroF[A]() extends PeanoF[A]

object PeanoF {

  implicit val functor: Functor[PeanoF] = instanceOf {
    new impl.Functor[PeanoF] {
      def map[A, B](ma: PeanoF[A])(f: A => B): PeanoF[B] =
        ma match {
          case SuccF(a) => SuccF(f(a))
          case ZeroF()  => ZeroF()
        }
    }
  }

  implicit val birecursive: Birecursive[PeanoF, Peano] =
    Fix.birecursive[PeanoF]

  implicit val eql: Eq[Peano] = impl.Eq.fromEquals
}

object Peano {

  val zero: Peano = Fix(ZeroF())

  def succ(p: Peano): Peano = Fix(SuccF(p))

  def toInt(p: Peano): Int =
    p.cata(count)

  def fromInt(i: Int): Peano =
    i.ana[Peano](uncount)

  val count: Algebra[PeanoF, Int] = {
    case SuccF(i) => 1 + i
    case ZeroF()  => 0
  }

  val uncount: Coalgebra[PeanoF, Int] = {
    case i if i <= 0 => ZeroF() // discard negative integers by making them 0
    case i if i > 0  => SuccF(i - 1)
  }
}
