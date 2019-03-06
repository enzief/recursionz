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

object coherent extends Coherent0

trait Coherent0 extends Coherent1 {
  implicit def applyFunctor[M[_]](implicit M:        Apply[M]):       Functor[M]     = instanceOf(M)
  implicit def bindApply[M[_]](implicit M:           Bind[M]):        Apply[M]       = instanceOf(M)
  implicit def monadBind[M[_]](implicit M:           Monad[M]):       Bind[M]        = instanceOf(M)
  implicit def monadApplicative[M[_]](implicit M:    Monad[M]):       Applicative[M] = instanceOf(M)
  implicit def monoidSemigroup[A](implicit A:        Monoid[A]):      Semigroup[A]   = instanceOf(A)
  implicit def traversableFoldable[T[_]](implicit T: Traversable[T]): Foldable[T]    = instanceOf(T)
  implicit def comonadCobind[F[_]](implicit F:       Comonad[F]):     Cobind[F]      = instanceOf(F)
}

trait Coherent1 {

  implicit def traversableFunctor[T[_]](implicit T: Traversable[T]): Functor[T] = instanceOf(T)
  implicit def applicativeApply[M[_]](implicit M:   Applicative[M]): Apply[M]   = instanceOf(M)

  implicit def applicativeErrorApplicative[F[_], E](
      implicit F: ApplicativeError[F, E]
  ): Applicative[F] =
    instanceOf(F)
}
