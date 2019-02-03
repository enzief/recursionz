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

trait ApplicativeErrorClass[F[_], S] extends ApplicativeClass[F] { self =>
  def raiseError[A](e: S): F[A]

  def handleError[A](fa: F[A])(f: S => F[A]): F[A]
}

object ApplicativeError {
  type ApplicativeError[F[_], E] = InstanceOf[ApplicativeErrorClass[F, E]]
  @inline def apply[F[_], S](implicit F: ApplicativeError[F, S]): ApplicativeError[F, S] = F
}
