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

sealed trait ListF[A, L]
final case class ConsF[A, L](head: A, tail: L) extends ListF[A, L]
final case class NilF[A, L]() extends ListF[A, L]

object ListF extends LiztInstances {

  def nil[A, L]: ListF[A, L] =
    NilF()

  def cons[A, L](a: A, t: L): ListF[A, L] =
    ConsF(a, t)

  implicit def instance[A]: Traversable[ListF[A, ?]] = instanceOf {
    new TraversableClass[ListF[A, ?]] {
      def map[L1, L2](ma: ListF[A, L1])(f: L1 => L2): ListF[A, L2] =
        ma match {
          case ConsF(head, tail) => ConsF(head, f(tail))
          case NilF()            => NilF()
        }

      def foldLeft[L1, L2](fa: ListF[A, L1], z: L2)(f: (L2, L1) => L2): L2 =
        fa match {
          case ConsF(_, t) => f(z, t)
          case NilF()      => z
        }

      def foldRight[L1, L2](fa: ListF[A, L1], z: => L2)(f: (L1, => L2) => L2): L2 =
        fa match {
          case ConsF(_, t) => f(t, z)
          case NilF()      => z
        }
    }
  }
}

object Lizt {

  def nil[A]: Lizt[A] =
    Fix(NilF())

  def cons[A](a: A, l: Lizt[A]): Lizt[A] =
    Fix(ConsF(a, l))

  def size[A]: Algebra[ListF[A, ?], Int] = {
    case ConsF(_, i) => 1 + i
    case NilF()      => 0
  }

  def concat[A](lz: Lizt[A]): AlgebraM[ListF[A, ?], Lizt, A] = {
    case ConsF(h, a) => h :: a :: lz
    case NilF()      => lz
  }

  def map[A, B](f: A => B): Algebra[ListF[A, ?], Lizt[B]] = {
    case ConsF(a, l) => cons(f(a), l)
    case NilF()      => nil
  }

  def bind[A, B](f: A => Lizt[B]): Algebra[ListF[A, ?], Lizt[B]] = {
    case ConsF(a, l) => f(a) ++ l
    case NilF()      => nil
  }

  implicit class Ops[A](private val lz: Lizt[A]) extends scala.AnyVal {

    def ::(a: A): Lizt[A] =
      cons(a, lz)

    def ++(that: Lizt[A]): Lizt[A] =
      RecursiveM[ListF[A, ?], Lizt[A]].cataM(lz)(Lizt.concat(that))

    def map[B](f: A => B): Lizt[B] =
      Functor[Lizt].map(lz)(f)

    def size: Int =
      Recursive[ListF[A, ?], Lizt[A]].cata(lz)(Lizt.size)
  }
}

trait LiztInstances {
  import scala.Predef.???

  implicit def liztMonad: Monad[Lizt] = instanceOf {
    new MonadClass[Lizt] {
      override def flatMap[A, B](ma: Lizt[A])(f: A => Lizt[B]): Lizt[B] =
        ???
//        ma match {
//          case Fix(ConsF(a, t)) => Fix(ConsF(f(a), map(t)(f)))
//          case Fix(NilF())      => Lizt.nil
//        }
    }
  }
}
