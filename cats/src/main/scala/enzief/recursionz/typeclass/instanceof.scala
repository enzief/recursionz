package enzief.recursionz

import scala.inline

// shamelessly copied and modified from Scalaz 8
sealed abstract class InstanceOfModule {
  type InstanceOf[T] <: T

  def instanceOf[T](t: T): InstanceOf[T]
}

object InstanceOfModule {

  val impl: InstanceOfModule = new InstanceOfModule {
    override type InstanceOf[T] = T

    @inline
    override def instanceOf[T](t: T) = t
  }
}
