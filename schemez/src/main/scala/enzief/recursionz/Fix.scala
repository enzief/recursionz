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

package enzief

package recursionz

import typeclass.Functor
import typeclass.Traversable
import typeclass.coherent.traversableFunctor

/** Simplest fixpoint type */
// shamelessly copied and modified from Scalaz 8
trait FixModule {
  type Fix[F[_]]

  def fix[F[_]](f: F[recursionz.Fix[F]]): Fix[F]

  def unfix[F[_]](f: Fix[F]): F[recursionz.Fix[F]]

  def subst[F[_[_[_]]]](f: F[λ[G[_] => G[recursionz.Fix[G]]]]): F[Fix]
}

object Fix {

  private[recursionz] val impl: FixModule = new FixModule {
    type Fix[F[_]] = F[recursionz.Fix[F]]

    def fix[F[_]](f: F[recursionz.Fix[F]]): Fix[F] = f

    def unfix[F[_]](f: Fix[F]): F[recursionz.Fix[F]] = f

    def subst[F[_[_[_]]]](f: F[λ[G[_] => G[recursionz.Fix[G]]]]): F[Fix] = f
  }

  def apply[F[_]](f: F[Fix[F]]): Fix[F] =
    impl.fix(f)

  def unapply[F[_]](f: Fix[F]): scala.Option[F[Fix[F]]] =
    scala.Some(impl.unfix(f))

  def subst[F[_[_[_]]]](f: F[λ[G[_] => G[Fix[G]]]]): F[Fix] =
    impl.subst(f)

  def birecursive[F[_]: Functor]: Birecursive[F, Fix[F]] =
    Birecursive.fromT[Fix, F](birecursiveT)

  def birecursiveM[F[_]: Traversable]: BirecursiveM[F, Fix[F]] =
    BirecursiveM.fromT[Fix, F](birecursiveT)

  def birecursiveT[F[_]](implicit F: Functor[F]): BirecursiveT[Fix, F] =
    new BirecursiveT[Fix, F] {
      def embedT(fa:  F[Fix[F]]): Fix[F]    = Fix(fa)
      def projectT(a: Fix[F]):    F[Fix[F]] = impl.unfix(a)
    }
}
