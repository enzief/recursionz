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

  def fromCoalgebra[F[_]: Traversable, A](cof: Coalgebra[F, A]): RecursiveM[F, A] =
    new RecursionzM[F] with RecursiveM[F, A] {
      val R: Recursive[F, A] = Recursive.fromCoalgebra[F, A](cof)(traversableFunctor(F))
    }

  /** Makes a `Recursive[F, T[F]` out of functor `F` and recursiveT `T` */
  def fromT[T[_[_]], F[_]](
      implicit
      F: Traversable[F],
      T: RecursiveT[T, F]
  ): RecursiveM[F, T[F]] =
    new RecursionzM[F] with RecursiveM[F, T[F]] {
      val R: Recursive[F, T[F]] = Recursive.fromT[T, F](traversableFunctor(F), T)
    }

  /** Dot syntax */
  implicit class Ops[A](private val a: A) extends scala.AnyVal {

    def cataM[F[_], M[_]: Monad, B](f: AlgebraM[F, M, B])(implicit F: RecursiveM[F, A]): M[B] =
      F.cataM(a)(f)
  }
}
