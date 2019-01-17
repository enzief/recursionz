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

import scalaz.tc.Functor

abstract class Recursive[F[_]: Functor, A] extends Recursionz[F] {

  def project(a: A): F[A]

  def cata[B](a: A)(f: Algebra[F, B]): B =
    hylo(a)(f, project)
}

object Recursive {

  def apply[F[_], A](implicit F: Recursive[F, A]): Recursive[F, A] = F

  /** Makes a `Recursive[F, T[F]` out of functor `F` and recursiveT `T` */
  def fromT[T[_[_]], F[_]](
      implicit
      F: Functor[F],
      T: RecursiveT[T, F]
  ): Recursive[F, T[F]] =
    new Recursive[F, T[F]] {
      def project(a: T[F]): F[T[F]] = T.projectT(a)
    }

  /** Dot syntax */
  implicit class Ops[A](private val a: A) extends scala.AnyVal {

    def project[F[_]](implicit F: Recursive[F, A]): F[A] =
      F.project(a)

    def cata[F[_], B](f: Algebra[F, B])(implicit F: Recursive[F, A]): B =
      F.cata(a)(f)
  }
}

/** Special `Recursive` for any HKT `T` that has an algebra `T[F] => F[T[F]]`
  * Eg: `Fix.unfix`.
  */
trait RecursiveT[T[_[_]], F[_]] {
  def projectT(t: T[F]): F[T[F]]
}

object RecursiveT {
  def apply[T[_[_]], F[_]](implicit T: RecursiveT[T, F]): RecursiveT[T, F] = T
}
