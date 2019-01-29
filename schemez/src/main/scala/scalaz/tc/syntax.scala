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

object syntax {

  implicit final class BindOps[F[_], A](private val fa: F[A]) extends scala.AnyVal {

    def >>=[B](f: A => F[B])(implicit F: Monad[F]): F[B] =
      F.flatMap(fa)(f)
  }

  implicit final class TraversableOps[F[_], G[_], A](private val fga: F[G[A]])
      extends scala.AnyVal {

    def sequence(implicit F: Traversable[F], G: Applicative[G]): G[F[A]] =
      F.sequence(fga)
  }
}
