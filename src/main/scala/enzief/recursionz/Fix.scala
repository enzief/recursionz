// Copyright (c) 2018 Yui Pham. All rights reserved.

package enzief.recursionz

/** Simplest fixpoint type */
final case class Fix[F[_]](unfix: F[Fix[F]])

object Fix {

  implicit def recursiveT[F[_]]: RecursiveT[Fix, F] =
    new RecursiveT[Fix, F] {
      def projectT(a: Fix[F]): F[Fix[F]] = a.unfix
    }
}
