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

import _root_.{cats => z}

trait TypeclassDefs {

  type Eq[A] = z.Eq[A]

  type Applicative[F[_]]         = z.Applicative[F]
  type ApplicativeError[F[_], E] = z.ApplicativeError[F, E]
  type Apply[F[_]]               = z.Apply[F]
  type Bind[F[_]]                = z.FlatMap[F]
  type Cobind[F[_]]              = z.CoflatMap[F]
  type Comonad[F[_]]             = z.Comonad[F]
  type Foldable[F[_]]            = z.Foldable[F]
  type Functor[F[_]]             = z.Functor[F]
  type Monad[F[_]]               = z.Monad[F]
  type Monoid[A]                 = z.Monoid[A]
  type Semigroup[A]              = z.Semigroup[A]
  type Traversable[F[_]]         = z.Traverse[F]
}
