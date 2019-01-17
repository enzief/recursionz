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

abstract class Corecursive[F[_]: Functor, A] extends Recursionz[F] {

  def embed(fa: F[A]): A

  def ana[B](b: B)(cof: Coalgebra[F, B]): A =
    hylo(b)(embed, cof)
}

object Corecursive {

  import scalaz.Scalaz._
  import scalaz.data.IList
  import scalaz.data.IListModule._
  import scalaz.tc.Monoid

  def ilist[A](implicit A: Monoid[A]): Corecursive[IList, A] = new Corecursive[IList, A] {

    def embed(fa: IList[A]): A =
      fa.foldLeft(A.mempty)(A.mappend(_, _))
  }

  def apply[F[_], A](implicit F: Corecursive[F, A]): Corecursive[F, A] = F

  /** Makes a `Corecursive[F, T[F]]` out of functor `F` and corecursiveT `T` */
  def fromT[T[_[_]], F[_]](
      implicit
      F: Functor[F],
      T: CorecursiveT[T, F]
  ): Corecursive[F, T[F]] =
    new Corecursive[F, T[F]] {
      def embed(fa: F[T[F]]): T[F] = T.embedT(fa)
    }

  /** Dot syntax */
  implicit class Ops[F[_], A](private val fa: F[A]) extends scala.AnyVal {

    def embed(implicit F: Corecursive[F, A]): A =
      F.embed(fa)
  }
}

/** Special `Corecursive` for any HKT `T` that has an coalgebra `F[T[F]] => T[F]`.
  * Eg. `Fix.apply`
  */
trait CorecursiveT[T[_[_]], F[_]] extends Recursionz[F] {

  def embedT(fa: F[T[F]]): T[F]
}
