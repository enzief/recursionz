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

package scalaz

package tc

import scalaz.Scalaz.traversableFunctor

class CompositionTraversableClass[F[_], G[_]](implicit F: Traversable[F], G: Traversable[G])
    extends CompositionFunctorClass[F, G]
    with TraversableClass[λ[α => F[G[α]]]] {

  def foldLeft[A, B](fga: F[G[A]], z: B)(f: (B, A) => B): B =
    F.foldLeft(fga, z) { (b, ga) =>
      G.foldLeft(ga, b)(f)
    }

  def foldRight[A, B](fga: F[G[A]], z: => B)(f: (A, => B) => B): B =
    F.foldRight(fga, z) { (ga, b) =>
      G.foldRight(ga, b)(f)
    }

  override def traverse[H[_]: Applicative, A, B](fga: F[G[A]])(f: A => H[B]): H[F[G[B]]] =
    F.traverse(fga)(G.traverse(_)(f))
}
