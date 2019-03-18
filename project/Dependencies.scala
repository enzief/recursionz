package enzief

import sbt._

object Dependencies {

  object CompilerPlugin {
    val kindProjector: ModuleID = "org.spire-math" %% "kind-projector"     % "0.9.9"
    val monadicFor:    ModuleID = "com.olegpy"     %% "better-monadic-for" % "0.3.0-M4"
  }

  object SbtPlugin {
    val coursier:           ModuleID = "io.get-coursier"   % "sbt-coursier"           % "1.1.0-M9"
    val dynver:             ModuleID = "com.dwijnand"      % "sbt-dynver"             % "3.1.0"
    val partialUnification: ModuleID = "org.lyranthe.sbt"  % "partial-unification"    % "1.1.2"
    val sbtHeader:          ModuleID = "de.heikoseeberger" % "sbt-header"             % "5.2.0"
    val sbtRevolver:        ModuleID = "io.spray"          % "sbt-revolver"           % "0.9.1"
    val scalafix:           ModuleID = "ch.epfl.scala"     % "sbt-scalafix"           % "0.9.1"
    val scalafmt:           ModuleID = "com.geirsson"      % "sbt-scalafmt"           % "1.6.0-RC4"
    val scalastyle:         ModuleID = "org.scalastyle"    %% "scalastyle-sbt-plugin" % "1.0.0"
  }

  val scaluzzi: ModuleID = "com.github.vovapolu" %% "scaluzzi" % "0.1.1"

  object Testing {
    val scalaCheck: ModuleID = "org.scalacheck" %% "scalacheck" % "1.14.0"
  }

  object Scalaz {
    val org:     String = "org.scalaz"
    val version: String = "ffd844c7+20190317-2220-SNAPSHOT"

    val core: ModuleID = org %% "scalaz-base" % version
  }
}
