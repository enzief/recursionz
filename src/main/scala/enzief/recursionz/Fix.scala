// Copyright (c) 2018 Yui Pham. All rights reserved.

package enzief.recursionz

import scalaz.tc.Functor

/** Simplest fixpoint type */
final case class Fix[F[_]](unfix: F[Fix[F]])

object Fix {

  implicit def recursiveT[F[_]: Functor]: RecursiveT[Fix, F] =
    new RecursiveT[Fix, F] {
      def projectT(a: Fix[F]): F[Fix[F]] = a.unfix
    }
}
