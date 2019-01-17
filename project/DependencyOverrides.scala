package enzief

import enzief.Dependencies._
import sbt._

/**
  * Used to manage library evictions
  */
object DependencyOverrides {

  val settings: Seq[ModuleID] = Seq(
    Scalaz.core
  )
}
