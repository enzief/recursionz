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

package typeclass

import scala.AnyVal
import scala.Option

import _root_.cats.{syntax => z}

object syntax
    extends z.ApplicativeSyntax
    with z.ApplySyntax
    with z.FlatMapSyntax
    with z.CoflatMapSyntax
    with z.ComonadSyntax
    with z.FoldableSyntax
    with z.FunctorSyntax
    with z.SemigroupSyntax
    with z.TraverseSyntax
    with z.SemigroupSyntax {

  implicit final class IdOps[A](private val a: A) extends AnyVal {

    def squared: (A, A) = (a, a)

    def |>[B](f: A => B): B = f(a)
  }

  implicit final class OptionOps[A](private val a: Option[A]) extends AnyVal {

    def liftTo[F[_]]: OptionSyntax[F, A] =
      new OptionSyntax(a)
  }

  final class OptionSyntax[F[_], A](private val a: Option[A]) extends AnyVal {

    def apply[E](e: E)(implicit F: ApplicativeError[F, E]): F[A] =
      a.map(F.pure).getOrElse(F.raiseError(e))
  }

  implicit final class BindOps[F[_], A](private val fa: F[A]) extends AnyVal {

    def >>=[B](f: A => F[B])(implicit F: Monad[F]): F[B] =
      F.flatMap(fa)(f)
  }

  implicit final class TraversableOps[F[_], G[_], A](private val fga: F[G[A]]) extends AnyVal {

    def sequence(implicit F: Traverse[F], G: Applicative[G]): G[F[A]] =
      F.sequence(fga)
  }
}
