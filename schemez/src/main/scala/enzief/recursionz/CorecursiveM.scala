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

import scalaz.Scalaz._
import scalaz.tc.Monad
import scalaz.tc.Traversable
import scalaz.tc.syntax._

trait CorecursiveM[F[_], A] { _: RecursionzM[F] =>

  /** `Traversable[F]` implies `Functor[F]` so that `this` implies `Recursive[F]`.
    */
  val R: Corecursive[F, A]

  def anaM[M[_]: Monad, B](b: B)(cof: CoalgebraM[F, M, B]): M[A] =
    hyloM(b)(R.embed(_).pure[M], cof)
}

object CorecursiveM {

  def apply[F[_], A](implicit F: CorecursiveM[F, A]): CorecursiveM[F, A] = F

  def fromAlgebra[F[_]: Traversable, A](f: Algebra[F, A]): CorecursiveM[F, A] =
    new RecursionzM[F] with CorecursiveM[F, A] {
      val R: Corecursive[F, A] = Corecursive.fromAlgebra[F, A](f)(traversableFunctor(F))
    }

  /** Makes a `Recursive[F, T[F]` out of functor `F` and recursiveT `T` */
  def fromT[T[_[_]], F[_]](
      implicit
      F: Traversable[F],
      T: CorecursiveT[T, F]
  ): CorecursiveM[F, T[F]] =
    new RecursionzM[F] with CorecursiveM[F, T[F]] {
      val R: Corecursive[F, T[F]] = Corecursive.fromT[T, F](traversableFunctor(F), T)
    }
}
