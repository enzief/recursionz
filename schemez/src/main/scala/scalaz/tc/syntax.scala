package scalaz

package tc

object syntax {

  implicit final class BindOps[F[_], A](private val fa: F[A]) extends scala.AnyVal {
    def >>=[B](f: A => F[B])(implicit F: Monad[F]): F[B] =
      F.flatMap(fa)(f)
  }

  implicit final class TraversableOps[F[_], G[_], A](private val fga: F[G[A]]) extends scala.AnyVal {
    def sequence(implicit F: Traversable[F], G: Applicative[G]): G[F[A]] =
      F.sequence(fga)
  }
}
