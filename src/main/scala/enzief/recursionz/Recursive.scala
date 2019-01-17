// Copyright (c) 2018 Yui Pham. All rights reserved.

package enzief.recursionz

import scalaz.Scalaz.Functor

trait Recursive[F[_]] {

  def project[A](a: A)(implicit F: Functor[F]): F[A]

  def cata[A, B](fa: F[A])(f: Algebra[F, B])(implicit F: Functor[F]): B =
    hylo(fa)(f, project[B])

  def hylo[A, B](fa: F[A])(
    f: Algebra[F, B],
    cof: Coalgebra[F, A]
  )(
    implicit F: Functor[F]
  ): B
}
