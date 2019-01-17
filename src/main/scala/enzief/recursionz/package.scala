// Copyright (c) 2018 Yui Pham. All rights reserved.

package enzief

import scalaz.Scalaz.Id

package object recursionz {

  type Algebra[F[_], A]        = AlgebraM[F, Id, A]
  type AlgebraM[F[_], M[_], A] = F[A] => M[A]

  type Coalgebra[F[_], A]        = CoalgebraM[F, Id, A]
  type CoalgebraM[F[_], M[_], A] = M[A] => F[A]
}
