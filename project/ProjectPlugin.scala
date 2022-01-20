package enzief

import scala.sys.process._
import scala.util._

import de.heikoseeberger.sbtheader.FileType
import de.heikoseeberger.sbtheader.HeaderPlugin, HeaderPlugin.autoImport._
import sbt._
import sbt.Keys._
import sbtdynver.DynVerPlugin
import scalafix.sbt.ScalafixPlugin.autoImport._

object ProjectPlugin extends AutoPlugin {

  override def requires: Plugins = HeaderPlugin && DynVerPlugin

  override def trigger = allRequirements

  override val buildSettings: Seq[Def.Setting[_]] = Seq(
    scalafixDependencies += Dependencies.scaluzzi,
    organization := "enzief",
    scalaVersion := "2.13.4",
  )

  override val projectSettings: Seq[Def.Setting[_]] =
    headerSettings ++
      Seq(
        conflictManager := ConflictManager.strict,
        dependencyOverrides := DependencyOverrides.settings,
        autoAPIMappings in Global := true,
        addCompilerPlugin(Dependencies.CompilerPlugin.kindProjector),
        addCompilerPlugin(Dependencies.CompilerPlugin.monadicFor),
        addCompilerPlugin(scalafixSemanticdb("4.4.33")),
        scalacOptions ++= commonScalacOptions ++ scalacOptionsFor212 ++ semanticdbOptions
      )

  private lazy val headerSettings: Seq[Def.Setting[_]] = Seq(
    startYear := Some(2018),
    licenses := Nil,
    headerLicense := Some(
      HeaderLicense.Custom(
        """Copyright (c) 2019 Yui Pham.
          |
          |Licensed under the Apache License, Version 2.0 (the "License");
          |you may not use this file except in compliance with the License.
          |You may obtain a copy of the License at
          |
          |http://www.apache.org/licenses/LICENSE-2.0
          |
          |Unless required by applicable law or agreed to in writing, software
          |"distributed under the License is distributed on an "AS IS" BASIS,
          |WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
          |See the License for the specific language governing permissions and
          |limitations under the License.
          |""".stripMargin
      )
    ),
    headerMappings := headerMappings.value ++ Map(
      FileType("sbt")      -> HeaderCommentStyle.cppStyleLineComment,
      HeaderFileType.java  -> HeaderCommentStyle.cppStyleLineComment,
      HeaderFileType.scala -> HeaderCommentStyle.cppStyleLineComment
    )
  )

  // See https://docs.scala-lang.org/overviews/compiler-options/index.html
  private lazy val optOptions: Seq[String] =
    if (sys.env.contains("HELM_VERSION")) {
      Seq(
        "-opt:box-unbox",
        "-opt:closure-invocations",
        "-opt:compact-locals",
        "-opt:copy-propagation",
        "-opt:nullness-tracking",
        "-opt:redundant-casts",
        "-opt:simplify-jumps",
        "-opt:unreachable-code",
        "-opt-warnings:_",
        "-opt:l:inline",
        "-opt:l:method",
        "-opt-inline-from:enzief.**:scalaz.**",
        "-Yopt-inline-heuristics:everything",
        "-Yopt-log-inline"
      )
    } else {
      Seq.empty
    }

  private lazy val scalacOptionsFor212: Seq[String] =
    Seq(
      "-Xlint:constant",
      "-Ywarn-extra-implicit",
      "-Ywarn-unused:implicits",
      "-Ywarn-unused:imports",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:params",
      "-Ywarn-unused:patvars",
      "-Ywarn-unused:privates"
    ) ++ optOptions

  private lazy val commonScalacOptions: Seq[String] =
    Seq(
      "-deprecation",
      "-encoding",
      "utf-8",
      "-explaintypes",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Xfuture",
      "-Xlint:adapted-args",
      "-Xlint:by-name-right-associative",
      "-Xlint:delayedinit-select",
      "-Xlint:doc-detached",
      "-Xlint:inaccessible",
      "-Xlint:infer-any",
      "-Xlint:missing-interpolator",
      "-Xlint:nullary-override",
      "-Xlint:nullary-unit",
      "-Xlint:option-implicit",
      "-Xlint:package-object-classes",
      "-Xlint:poly-implicit-overload",
      "-Xlint:private-shadow",
      "-Xlint:stars-align",
      "-Xlint:type-parameter-shadow",
      "-Xlint:unsound-match",
      "-Yno-adapted-args",
      "-Yno-imports",
      "-Ypartial-unification",
      "-Yrangepos",
      "-Ywarn-dead-code",
      "-Ywarn-inaccessible",
      "-Ywarn-infer-any",
      "-Ywarn-nullary-override",
      "-Ywarn-nullary-unit",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard"
    )

  private lazy val semanticdbOptions: Seq[String] =
    Seq(
      "-P:semanticdb:exclude:Macros.scala"
    )
}
