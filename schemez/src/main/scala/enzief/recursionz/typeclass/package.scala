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

package object typeclass extends TypeclassDefs {

  final def Eq[A](implicit A: Eq[A]): Eq[A] = A

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
}
