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

import scala.AnyVal

import typeclass.Monad

object syntax {

  implicit class recursionz[A](private val a: A) extends AnyVal {

    def hylo[F[_], B](f: Algebra[F, B], cof: Coalgebra[F, A])(implicit F: Recursionz[F]): B =
      F.hylo(a)(f, cof)
  }

  implicit class recursionzM[A](private val a: A) extends AnyVal {

    def hyloM[F[_], M[_]: Monad, B](
        f:   AlgebraM[F, M, B],
        cof: CoalgebraM[F, M, A]
    )(
        implicit F: RecursionzM[F]
    ): M[B] =
      F.hyloM(a)(f, cof)
  }

  implicit class recursive[A](private val a: A) extends AnyVal {

    def project[F[_]](implicit F: Recursive[F, A]): F[A] =
      F.project(a)

    def cata[F[_], B](f: Algebra[F, B])(implicit F: Recursive[F, A]): B =
      F.cata(a)(f)

    def para[F[_], B](f: GAlgebra[F, (A, ?), B])(implicit F: Recursive[F, A]): B =
      F.para(a)(f)
  }

  implicit class recursiveM[A](private val a: A) extends AnyVal {

    def cataM[F[_], M[_]: Monad, B](f: AlgebraM[F, M, B])(implicit F: RecursiveM[F, A]): M[B] =
      F.cataM(a)(f)
  }

  implicit class corecursive[B](private val b: B) extends AnyVal {
    import scalaz.prop.===

    def embed[F[_], A](implicit I: B === F[A], F: Corecursive[F, A]): A =
      F.embed(I(b))

    def ana[A]: AnaSyntax[B, A] = new AnaSyntax[B, A](b)
  }

  class AnaSyntax[B, A](private val b: B) extends AnyVal {

    def apply[F[_]](cof: Coalgebra[F, B])(implicit F: Corecursive[F, A]): A =
      F.ana(b)(cof)
  }

  implicit class corecursiveM[B](private val b: B) extends AnyVal {

    def anaM[A]: AnaMSyntax[B, A] = new AnaMSyntax[B, A](b)
  }

  class AnaMSyntax[B, A](private val b: B) extends AnyVal {

    def apply[F[_], M[_]: Monad](cof: CoalgebraM[F, M, B])(implicit F: CorecursiveM[F, A]): M[A] =
      F.anaM(b)(cof)
  }
}
