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

import typeclass.Functor
import typeclass.coherent._
import typeclass.syntax._

trait Recursive[F[_], A] { _: Recursionz[F] =>

  def project(a: A): F[A]

  def cata[B](a: A)(f: Algebra[F, B]): B =
    hylo(a)(f, project)

  def para[B](a: A)(f: GAlgebra[F, (A, ?), B]): B =
    compose[(A, ?)]
      .hylo[A, B](a)(f, project(_).map(_.squared))
}

object Recursive {

  def apply[F[_], A](implicit F: Recursive[F, A]): Recursive[F, A] = F

  def fromCoalgebra[F[_]: Functor, A](cof: Coalgebra[F, A]): Recursive[F, A] =
    new Recursionz[F] with Recursive[F, A] {
      def project(a: A): F[A] = cof(a)
    }

  implicit def fromM[F[_], A](implicit F: RecursiveM[F, A]): Recursive[F, A] = F.R

  /** Makes a `Recursive[F, T[F]` out of functor `F` and recursiveT `T` */
  def fromT[T[_[_]], F[_]: Functor](T: RecursiveT[T, F]): Recursive[F, T[F]] =
    new Recursionz[F] with Recursive[F, T[F]] {
      def project(a: T[F]): F[T[F]] = T.projectT(a)
    }
}

/** Special `Recursive` for any HKT `T` that has an algebra `T[F] => F[T[F]]` Eg: `Fix.unfix`.
  */
trait RecursiveT[T[_[_]], F[_]] { _: Recursionz[F] =>
  def projectT(t: T[F]): F[T[F]]
}

object RecursiveT {
  def apply[T[_[_]], F[_]](implicit T: RecursiveT[T, F]): RecursiveT[T, F] = T
}
