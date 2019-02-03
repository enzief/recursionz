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

import scalaz.{tc => z}

package object typeclass {

  type Applicative[F[_]] = z.Applicative[F]
  type Apply[F[_]]       = z.Apply[F]
  type Bind[F[_]]        = z.Bind[F]
  type Cobind[F[_]]      = z.Cobind[F]
  type Comonad[F[_]]     = z.Comonad[F]
  type Foldable[F[_]]    = z.Foldable[F]
  type Functor[F[_]]     = z.Functor[F]
  type Monad[F[_]]       = z.Monad[F]
  type Monoid[A]         = z.Monoid[A]
  type Semigroup[A]      = z.Semigroup[A]
  type Traversable[F[_]] = z.Traversable[F]

  type ApplicativeError[F[_], E] = z.ApplicativeError.ApplicativeError[F, E]

  final def Applicative[F[_]](implicit F: Applicative[F]): Applicative[F] = F
  final def Apply[F[_]](implicit F:       Apply[F]):       Apply[F]       = F
  final def Bind[F[_]](implicit F:        Bind[F]):        Bind[F]        = F
  final def Cobind[F[_]](implicit F:      Cobind[F]):      Cobind[F]      = F
  final def Comonad[F[_]](implicit F:     Comonad[F]):     Comonad[F]     = F
  final def Foldable[F[_]](implicit F:    Foldable[F]):    Foldable[F]    = F
  final def Functor[F[_]](implicit F:     Functor[F]):     Functor[F]     = F
  final def Monad[F[_]](implicit F:       Monad[F]):       Monad[F]       = F
  final def Monoid[A](implicit A:         Monoid[A]):      Monoid[A]      = A
  final def Semigroup[A](implicit A:      Semigroup[A]):   Semigroup[A]   = A
  final def Traversable[F[_]](implicit F: Traversable[F]): Traversable[F] = F

  final def ApplicativeError[F[_], E](
      implicit F: ApplicativeError[F, E]
  ): ApplicativeError[F, E] =
    F

  final def Functor[F[_]](F:     impl.Functor[F]):     Functor[F]     = z.instanceOf(F)
  final def Monad[F[_]](F:       impl.Monad[F]):       Monad[F]       = z.instanceOf(F)
  final def Monoid[A](A:         impl.Monoid[A]):      Monoid[A]      = z.instanceOf(A)
  final def Traversable[F[_]](F: impl.Traversable[F]): Traversable[F] = z.instanceOf(F)

  final def ApplicativeError[F[_], E](F: impl.ApplicativeError[F, E]): ApplicativeError[F, E] =
    z.instanceOf(F)
}
