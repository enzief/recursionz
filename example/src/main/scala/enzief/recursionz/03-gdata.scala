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

package example

import scala.List
import scala.Predef.Map

import scalaz.Predef._

import enzief.recursionz.typeclass._
import enzief.recursionz.typeclass.coherent._
import enzief.recursionz.typeclass.syntax._

sealed trait RawData[A]
case class RawObj[A](fields: Map[String, A]) extends RawData[A]
case class RawList[A](elems: List[A]) extends RawData[A]
case class RawInt[A](x:      Int) extends RawData[A]
case class RawLong[A](x:     Long) extends RawData[A]
case class RawFloat[A](x:    Float) extends RawData[A]
case class RawDouble[A](x:   Double) extends RawData[A]
case class RawString[A](x:   String) extends RawData[A]

object RawData {

  implicit val instances: Traversable[RawData] = Traversable {
    new impl.Traversable[RawData] {
      def map[A, B](ma: RawData[A])(f: A => B): RawData[B] =
        ma match {
          case RawObj(fields) => RawObj(fields.mapValues(f))
          case RawList(elems) => RawList(elems.map(f))
          case x              => x.asInstanceOf[RawData[B]]
        }

      override def traverse[F[_], A, B](ta: RawData[A])(f: A => F[B])(
          implicit
          F: Applicative[F]
      ): F[RawData[B]] =
        ta match {
          case RawObj(fields) => fields.toList.traverse(_.traverse(f)).map(_.toMap |> RawObj[B])
          case RawList(elems) => elems.traverse(f).map(RawList[B])
          case x              => x.asInstanceOf[RawData[B]].pure[F]
        }

      def foldLeft[A, B](fa: RawData[A], z: B)(f: (B, A) => B): B =
        fa match {
          case RawObj(fields) => fields.foldLeft(z)((b, a) => f(b, a._2))
          case RawList(elems) => elems.foldLeft(z)(f)
          case _              => z
        }

      def foldRight[A, B](fa: RawData[A], z: => B)(f: (A, => B) => B): B =
        fa match {
          case RawObj(fields) => fields.foldRight(z)((a, b) => f(a._2, b))
          case RawList(elems) => elems.foldRight(z)(f(_, _))
          case _              => z
        }
    }
  }
}
