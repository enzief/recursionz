// Copyright (c) 2018 Yui Pham. All rights reserved.

package enzief.recursionz

import scalaz.Scalaz.ToFunctorOps
import scalaz.tc.Functor

abstract class Recursive[F[_]: Functor, A] {

  def project(a: A): F[A]

  def cata[B](a: A)(f: Algebra[F, B]): B =
    hylo(a)(f, project)

  def hylo[B](a: A)(f: Algebra[F, B], cof: Coalgebra[F, A]): B =
    f(cof(a).map(hylo(_)(f, cof)))
}

object Recursive {

  def apply[F[_], A](implicit F: Recursive[F, A]) = F

  implicit def fromT[T[_[_]], F[_]](
      implicit
      F: Functor[F],
      T: RecursiveT[T, F]
  ): Recursive[F, T[F]] =
    new Recursive[F, T[F]] {
      def project(a: T[F]): F[T[F]] = T.projectT(a)
    }
}

abstract class RecursiveT[T[_[_]], F[_]] {
  def projectT(t: T[F]): F[T[F]]
}

object RecursiveT {
  def apply[T[_[_]], F[_]](implicit T: RecursiveT[T, F]) = T
}
