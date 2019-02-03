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

object instances {

  implicit def either[E]: ApplicativeError[Either[E, ?], E] = ApplicativeError {
    new impl.ApplicativeError[Either[E, ?], E] {

      def raiseError[A](e: E): Either[E, A] =
        Left(e)

      def handleError[A](fa: Either[E, A])(f: E => Either[E, A]): Either[E, A] =
        fa match {
          case Right(_) => fa
          case Left(e)  => f(e)
        }
    }
  }
}
