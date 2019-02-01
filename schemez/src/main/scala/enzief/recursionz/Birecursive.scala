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

abstract class Birecursive[F[_], A](implicit F: Functor[F])
    extends Recursionz[F]
    with Recursive[F, A]
    with Corecursive[F, A]

object Birecursive {
  def apply[F[_], A](implicit F: Birecursive[F, A]): Birecursive[F, A] = F

  def fromAlgebraIso[F[_]: Functor, A](f: Algebra[F, A], cof: Coalgebra[F, A]): Birecursive[F, A] =
    new Birecursive[F, A] {
      def embed(fa:  F[A]): A    = f(fa)
      def project(a: A):    F[A] = cof(a)
    }

  implicit def fromM[F[_], A](implicit F: BirecursiveM[F, A]): Birecursive[F, A] = F.R

  /** Makes a `Birecursive[F, T[F]]` out of functor `F` and birecursiveT `T` */
  def fromT[T[_[_]], F[_]: Functor](T: BirecursiveT[T, F]): Birecursive[F, T[F]] =
    new Birecursive[F, T[F]] {
      def embed(fa:  F[T[F]]): T[F]    = T.embedT(fa)
      def project(a: T[F]):    F[T[F]] = T.projectT(a)
    }
}

/** Special `Birecursive` for any HKT `T` that has
  * - an algebra `T[F] => F[T[F]]`, eg: `Fix.unfix`
  * - and an coalgebra `F[T[F]] => T[F]`, eg. `Fix.apply`
  */
abstract class BirecursiveT[T[_[_]], F[_]](implicit F: Functor[F])
    extends Recursionz[F]
    with RecursiveT[T, F]
    with CorecursiveT[T, F]

object BirecursiveT {
  def apply[T[_[_]], F[_]](implicit T: BirecursiveT[T, F]): BirecursiveT[T, F] = T
}
