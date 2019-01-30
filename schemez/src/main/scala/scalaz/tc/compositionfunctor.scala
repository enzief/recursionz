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

class CompositionFunctor[F[_], G[_]](implicit F: Functor[F], G: Functor[G])
    extends FunctorClass[λ[α => F[G[α]]]] {

  def map[A, B](fga: F[G[A]])(f: A => B): F[G[B]] =
    F.map(fga)(G.map(_)(f))
}
