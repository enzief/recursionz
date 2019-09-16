package enzief

import sbt._

object Dependencies {

  object CompilerPlugin {
    val kindProjector: ModuleID = "org.spire-math" %% "kind-projector"     % "0.9.9"
    val monadicFor:    ModuleID = "com.olegpy"     %% "better-monadic-for" % "0.3.0"
  }

  object SbtPlugin {
    val coursier:           ModuleID = "io.get-coursier"   % "sbt-coursier"           % "1.1.0-M13-3"
    val dynver:             ModuleID = "com.dwijnand"      % "sbt-dynver"             % "3.3.0"
    val partialUnification: ModuleID = "org.lyranthe.sbt"  % "partial-unification"    % "1.1.2"
    val sbtHeader:          ModuleID = "de.heikoseeberger" % "sbt-header"             % "5.2.0"
    val scalafix:           ModuleID = "ch.epfl.scala"     % "sbt-scalafix"           % "0.9.4"
    val scalafmt:           ModuleID = "org.scalameta"     % "sbt-scalafmt"           % "2.0.5"
    val scalastyle:         ModuleID = "org.scalastyle"    %% "scalastyle-sbt-plugin" % "1.0.0"
  }

  val scaluzzi: ModuleID = "com.github.vovapolu" %% "scaluzzi" % "0.1.1"

  object Testing {
    val scalaCheck: ModuleID = "org.scalacheck" %% "scalacheck" % "1.14.0"
  }

  object Scalaz {
    val org:     String = "org.scalaz"
    val version: String = "7cf830e4-SNAPSHOT"

    val core: ModuleID = org %% "scalaz-base" % version
    val laws: ModuleID = org %% "scalaz-laws" % version

    object Testz {
      val version: String = "0.0.5"

      val testz: ModuleID = "org.scalaz" %% "testz-stdlib" % version
    }
  }
}
