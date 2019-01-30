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
          case ConsF(h, t) => ConsF(h, f(t))
          case NilF()      => NilF()
        }

      override def traverse[F[_], L1, L2](ta: ListF[A, L1])(f: L1 => F[L2])(
          implicit
          F: Applicative[F]
      ): F[ListF[A, L2]] =
        ta match {
          case ConsF(h, t) => f(t).map(ConsF(h, _))
          case NilF()      => F.pure(NilF())
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

  /** Put it here for implicit search.
    */
  implicit class Ops[A](private val lz: Lizt[A]) extends scala.AnyVal {

    def ::(a: A): Lizt[A] =
      Lizt.cons(a, lz)

    def ++(that: Lizt[A]): Lizt[A] =
      Recursive[ListF[A, ?], Lizt[A]].cata(lz)(Lizt.concat(that))

    def map[B](f: A => B): Lizt[B] =
      Functor[Lizt].map(lz)(f)

    def size: Int =
      Recursive[ListF[A, ?], Lizt[A]].cata(lz)(Lizt.size)
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

  def concat[A](lz: Lizt[A]): Algebra[ListF[A, ?], Lizt[A]] = {
    case ConsF(h, l) => h :: l
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
}

trait LiztInstances {

  implicit def liztMonad: Monad[Lizt] = instanceOf {
    new MonadClass[Lizt] {

      override def pure[A](a: A): Lizt[A] =
        Lizt.cons(a, Lizt.nil)

      override def map[A, B](ma: Lizt[A])(f: A => B): Lizt[B] =
        ma match {
          case Fix(ConsF(a, t)) => f(a) :: map(t)(f)
          case Fix(NilF())      => Lizt.nil
        }

      override def flatMap[A, B](ma: Lizt[A])(f: A => Lizt[B]): Lizt[B] =
        ma match {
          case Fix(ConsF(a, t)) => concat(f(a), flatMap(t)(f))
          case Fix(NilF())      => Lizt.nil
        }

      private def concat[A](l1: Lizt[A], l2: Lizt[A]): Lizt[A] =
        l1 match {
          case Fix(ConsF(h, t)) => h :: concat(t, l2)
          case Fix(NilF())      => l2
        }
    }
  }
}
