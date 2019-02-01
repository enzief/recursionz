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

import typeclass.Traversable
import typeclass.coherent.traversableFunctor

abstract class BirecursiveM[F[_], A](implicit F: Traversable[F])
    extends RecursionzM[F]
    with RecursiveM[F, A]
    with CorecursiveM[F, A] {

  /** `Traversable[F]` implies `Functor[F]` so that `this` implies `Birecursive[F]`.
    */
  override val R: Birecursive[F, A]
}

object BirecursiveM {
  def apply[F[_], A](implicit F: BirecursiveM[F, A]): BirecursiveM[F, A] = F

  def fromAlgebraIso[F[_], A](
      f:   Algebra[F, A],
      cof: Coalgebra[F, A]
  )(
      implicit
      F: Traversable[F]
  ): BirecursiveM[F, A] =
    new BirecursiveM[F, A] {
      override val R: Birecursive[F, A] = Birecursive.fromAlgebraIso[F, A](f, cof)
    }

  /** Makes a `BirecursiveM[F, T[F]]` out of traversable `F` and birecursiveT `T` */
  def fromT[T[_[_]], F[_]](T: BirecursiveT[T, F])(
      implicit
      F: Traversable[F]
  ): BirecursiveM[F, T[F]] =
    new BirecursiveM[F, T[F]] {
      override val R: Birecursive[F, T[F]] = Birecursive.fromT[T, F](T)
    }
}
