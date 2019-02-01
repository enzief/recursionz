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

import scalaz.data.Maybe
import scalaz.data.MaybeModule._
import scalaz.Predef.Int

import enzief.recursionz.syntax._
import enzief.recursionz.typeclass._
import enzief.recursionz.typeclass.coherent._
import enzief.recursionz.typeclass.syntax._

sealed trait ListF[A, L]
final case class ConsF[A, L](head: A, tail: L) extends ListF[A, L]
final case class NilF[A, L]() extends ListF[A, L]

object ListF extends LiztInstances {

  def nil[A, L]: ListF[A, L] =
    NilF()

  def cons[A, L](a: A, t: L): ListF[A, L] =
    ConsF(a, t)

  def size[A]: Algebra[ListF[A, ?], Int] = {
    case ConsF(_, i) => 1 + i
    case NilF()      => 0
  }

  def concat[A](lz: Lizt[A]): Algebra[ListF[A, ?], Lizt[A]] = {
    case ConsF(h, l) => h :: l
    case NilF()      => lz
  }

  def reduce[A: Semigroup]: Algebra[ListF[A, ?], Maybe[A]] = {
    case ConsF(a1, a2) => Semigroup[Maybe[A]].mappend(Maybe.just(a1), a2)
    case NilF()        => Maybe.empty
  }

  def reverse[A]: Algebra[ListF[A, ?], Lizt[A]] = {
    case ConsF(h, l) => l ++ (h :: Lizt.nil)
    case NilF()      => Lizt.nil
  }

  implicit def instances[A]: Traversable[ListF[A, ?]] = Traversable {
    type ListFA[B] = ListF[A, B]
    new impl.Traversable[ListFA] {
      def map[L1, L2](ma: ListFA[L1])(f: L1 => L2): ListFA[L2] =
        ma match {
          case ConsF(h, t) => ConsF(h, f(t))
          case NilF()      => NilF()
        }

      override def traverse[F[_], L1, L2](ta: ListFA[L1])(f: L1 => F[L2])(
          implicit
          F: Applicative[F]
      ): F[ListFA[L2]] =
        ta match {
          case ConsF(h, t) => f(t).map(ConsF(h, _))
          case NilF()      => F.pure(NilF())
        }

      def foldLeft[L1, L2](fa: ListFA[L1], z: L2)(f: (L2, L1) => L2): L2 =
        fa match {
          case ConsF(_, t) => f(z, t)
          case NilF()      => z
        }

      def foldRight[L1, L2](fa: ListFA[L1], z: => L2)(f: (L1, => L2) => L2): L2 =
        fa match {
          case ConsF(_, t) => f(t, z)
          case NilF()      => z
        }
    }
  }

  implicit def birecursiveM[A]: BirecursiveM[ListF[A, ?], Lizt[A]] =
    Fix.birecursiveM[ListF[A, ?]]

  /** Put it here for implicit search.
    */
  implicit class Ops[A](private val lz: Lizt[A]) extends scala.AnyVal {
    type ListFA[B] = ListF[A, B]

    def ::(a: A): Lizt[A] =
      Lizt.cons(a, lz)

    def ++(that: Lizt[A]): Lizt[A] =
      lz.cata(ListF.concat(that))

    def map[B](f: A => B): Lizt[B] =
      Functor[Lizt].map(lz)(f)

    def >>=[B](f: A => Lizt[B]): Lizt[B] =
      Monad[Lizt].flatMap(lz)(f)

    def reduce(implicit A: Semigroup[A]): Maybe[A] =
      lz.cata(ListF.reduce[A])

    def reverse: Lizt[A] =
      lz.cata(ListF.reverse[A])

    def size: Int =
      lz.cata(ListF.size[A])
  }
}

object Lizt {

  def nil[A]: Lizt[A] =
    Fix(NilF())

  def cons[A](a: A, l: Lizt[A]): Lizt[A] =
    Fix(ConsF(a, l))
}

trait LiztInstances {
  // https://github.com/scala/bug/issues/11389
  // remove import when it's fixed
  // appearently it works with 2.13.x
  import ListF.Ops

  implicit def liztMonad:       Monad[Lizt]       = Monad(instances)
  implicit def liztTraversable: Traversable[Lizt] = Traversable(instances)

  implicit def monoid[A]: Monoid[Lizt[A]] = Monoid {
    new impl.Monoid[Lizt[A]] {

      def mempty: Lizt[A] = Lizt.nil

      def mappend(l1: Lizt[A], l2: => Lizt[A]): Lizt[A] =
        concat(l1, l2)
    }
  }

  private val instances: impl.Monad[Lizt] with impl.Traversable[Lizt] =
    new impl.Monad[Lizt] with impl.Traversable[Lizt] {

      override def map[A, B](ma: Lizt[A])(f: A => B): Lizt[B] =
        ma match {
          case Fix(ConsF(a, t)) => f(a) :: map(t)(f)
          case Fix(NilF())      => Lizt.nil
        }

      override def pure[A](a: A): Lizt[A] =
        Lizt.cons(a, Lizt.nil)

      override def flatMap[A, B](ma: Lizt[A])(f: A => Lizt[B]): Lizt[B] =
        ma match {
          case Fix(ConsF(a, t)) => concat(f(a), flatMap(t)(f))
          case Fix(NilF())      => Lizt.nil
        }

      override def foldLeft[A, B](ma: Lizt[A], b: B)(f: (B, A) => B): B =
        ma match {
          case Fix(ConsF(a, t)) => f(foldLeft(t, b)(f), a)
          case Fix(NilF())      => b
        }

      override def foldRight[A, B](ma: Lizt[A], b: => B)(f: (A, => B) => B): B =
        foldLeft(ma.reverse, b)((b, a) => f(a, b))

      override def traverse[F[_], A, B](ma: Lizt[A])(f: A => F[B])(
          implicit
          F: Applicative[F]
      ): F[Lizt[B]] =
        ma match {
          case Fix(ConsF(a, t)) => f(a).liftA2(traverse(t)(f))(Lizt.cons)
          case Fix(NilF())      => F.pure(Lizt.nil[B])
        }
    }

  private def concat[A](l1: Lizt[A], l2: Lizt[A]): Lizt[A] =
    l1 match {
      case Fix(ConsF(h, t)) => h :: concat(t, l2)
      case Fix(NilF())      => l2
    }
}
