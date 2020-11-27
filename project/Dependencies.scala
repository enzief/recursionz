package enzief

import sbt._

object Dependencies {

  object CompilerPlugin {
    val kindProjector: ModuleID = Typelevel.kindProjector
    val monadicFor: ModuleID    = "com.olegpy" %% "better-monadic-for" % "0.3.1"
  }

  object SbtPlugin {
    val dynver: ModuleID    = "com.dwijnand"      % "sbt-dynver"   % "4.1.1"
    val sbtHeader: ModuleID = "de.heikoseeberger" % "sbt-header"   % "5.6.0"
    val scalafix: ModuleID  = "ch.epfl.scala"     % "sbt-scalafix" % "0.9.23"
    val scalafmt: ModuleID  = "org.scalameta"     % "sbt-scalafmt" % "2.4.2"
  }

  val scaluzzi: ModuleID = "com.github.vovapolu" %% "scaluzzi" % "0.1.16"

  object Testing {
    val scalaCheck: ModuleID = "org.scalacheck" %% "scalacheck" % "1.15.1"
  }

  object Typelevel {
    val org: String = "org.typelevel"

    val kindProjector: ModuleID = org %% "kind-projector" % "0.11.1" cross CrossVersion.full

    object Cats {
      def version: String = "2.3.0"

      def core: ModuleID   = org %% "cats-core"   % version
      def kernel: ModuleID = org %% "cats-kernel" % version
    }
  }
}
