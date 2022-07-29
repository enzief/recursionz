package enzief

import sbt._

object Dependencies {

  object CompilerPlugin {
    val kindProjector: ModuleID = "org.typelevel" %% "kind-projector"     % "0.11.1" cross CrossVersion.full
    val monadicFor: ModuleID    = "com.olegpy"    %% "better-monadic-for" % "0.3.1"
  }

  object SbtPlugin {
    val dynver: ModuleID    = "com.dwijnand"      % "sbt-dynver"   % "4.1.1"
    val sbtHeader: ModuleID = "de.heikoseeberger" % "sbt-header"   % "5.6.0"
    val scalafix: ModuleID  = "ch.epfl.scala"     % "sbt-scalafix" % "0.9.23"
    val scalafmt: ModuleID  = "org.scalameta"     % "sbt-scalafmt" % "2.4.2"
  }

  val scaluzzi: ModuleID = "com.github.vovapolu" %% "scaluzzi" % "0.1.23"

  object Testing {
    val scalaCheck: ModuleID = "org.scalacheck" %% "scalacheck" % "1.15.1"
  }

  object Scalaz {
    val org: String     = "org.scalaz"
    val version: String = "7cf830e4-SNAPSHOT"

    val core: ModuleID = org %% "scalaz-base" % version
    val laws: ModuleID = org %% "scalaz-laws" % version

    object Testz {
      val version: String = "0.0.5"

      val testz: ModuleID = "org.scalaz" %% "testz-stdlib" % version
    }
  }
}
