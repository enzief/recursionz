package enzief.recursionz

package example

import scala.Nothing
import scala.collection.immutable.ListMap

import scalaz.Predef._

import enzief.recursionz.typeclass._
import enzief.recursionz.typeclass.coherent._
import enzief.recursionz.typeclass.syntax._

sealed trait SchemaF[+A]
final case class ObjectF[A](fields: ListMap[String, A]) extends SchemaF[A]
final case class ArrayF[A](elems:   A) extends SchemaF[A]
object BooleanF extends SchemaF[Nothing]
final case class DoubleF[A]() extends SchemaF[A]
final case class FloatF[A]() extends SchemaF[A]
final case class IntF[A]() extends SchemaF[A]
final case class LongF[A]() extends SchemaF[A]
final case class StringF[A]() extends SchemaF[A]

object SchemaF {

  implicit val instances: Traversable[SchemaF] = instanceOf {
    new impl.Traversable[SchemaF] {
      def map[A, B](ma: SchemaF[A])(f: A => B): SchemaF[B] =
        ma match {
          case ObjectF(fields) => ListMap(fields.mapValues(f).toSeq: _*) |> ObjectF.apply
          case ArrayF(elems)   => ArrayF(f(elems))
          case x               => x.asInstanceOf[SchemaF[B]]
        }

      override def traverse[F[_], A, B](ta: SchemaF[A])(f: A => F[B])(
          implicit
          F: Applicative[F]
      ): F[SchemaF[B]] =
        ta match {
          case ObjectF(fields) =>
            fields.toList.traverse(_.traverse(f)).map(ListMap(_: _*) |> ObjectF[B])
          case ArrayF(elems) => f(elems).map(ArrayF[B])
          case x             => x.asInstanceOf[SchemaF[B]].pure[F]
        }

      def foldLeft[A, B](fa: SchemaF[A], z: B)(f: (B, A) => B): B =
        fa match {
          case ObjectF(fields) => fields.foldLeft(z)((b, a) => f(b, a._2))
          case ArrayF(elems)   => f(z, elems)
          case _               => z
        }

      def foldRight[A, B](fa: SchemaF[A], z: => B)(f: (A, => B) => B): B =
        fa match {
          case ObjectF(fields) => fields.foldRight(z)((a, b) => f(a._2, b))
          case ArrayF(elems)   => f(elems, z)
          case _               => z
        }
    }
  }
}
