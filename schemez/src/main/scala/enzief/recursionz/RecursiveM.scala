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

trait RecursiveM[F[_], A] { _: RecursionzM[F] =>

  /** `Traversable[F]` implies `Functor[F]` so that `this` implies `Recursive[F]`.
    */
  val R: Recursive[F, A]

  def cataM[M[_]: Monad, B](a: A)(f: AlgebraM[F, M, B]): M[B] =
    R.cata[M[B]](a)(_.sequence >>= f)
}

object RecursiveM {

  def apply[F[_], A](implicit F: RecursiveM[F, A]): RecursiveM[F, A] = F

  def fromCoalgebra[F[_], A](
      cof: Coalgebra[F, A]
  )(
      implicit
      F: Traversable[F] // named as F is mandatory for better implicit shadowing
  ): RecursiveM[F, A] =
    new RecursionzM[F] with RecursiveM[F, A] {
      val R: Recursive[F, A] = Recursive.fromCoalgebra[F, A](cof)
    }

  /** Makes a `RecursiveM[F, T[F]` out of traversable `F` and recursiveT `T` */
  def fromT[T[_[_]], F[_]](T: RecursiveT[T, F])(
      implicit
      F: Traversable[F] // named as F is mandatory for better implicit shadowing
  ): RecursiveM[F, T[F]] =
    new RecursionzM[F] with RecursiveM[F, T[F]] {
      val R: Recursive[F, T[F]] = Recursive.fromT[T, F](T)
    }
}
