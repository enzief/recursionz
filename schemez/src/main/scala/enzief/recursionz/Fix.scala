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
import typeclass.Traversable
import typeclass.coherent.traversableFunctor

/** Simplest fixpoint type */
final case class Fix[F[_]](unfix: F[Fix[F]])

object Fix {

  implicit def birecursive[F[_]: Functor]: Birecursive[F, Fix[F]] =
    Birecursive.fromT[Fix, F]

  implicit def birecursiveM[F[_]: Traversable]: BirecursiveM[F, Fix[F]] =
    BirecursiveM.fromT[Fix, F]

  implicit def birecursiveT[F[_]](implicit F: Functor[F]): BirecursiveT[Fix, F] =
    new BirecursiveT[Fix, F] {
      def embedT(fa:  F[Fix[F]]): Fix[F]    = Fix(fa)
      def projectT(a: Fix[F]):    F[Fix[F]] = a.unfix
    }
}
