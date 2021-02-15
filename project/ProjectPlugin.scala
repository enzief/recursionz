package enzief

import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport._
import de.heikoseeberger.sbtheader.{FileType, HeaderPlugin}
import sbt.Keys._
import sbt._
import sbtdynver.DynVerPlugin
import scalafix.sbt.ScalafixPlugin.autoImport._

object ProjectPlugin extends AutoPlugin {

  override def requires: Plugins = HeaderPlugin && DynVerPlugin

  override def trigger = allRequirements

  override val buildSettings: Seq[Def.Setting[_]] = Seq(
    scalafixDependencies += Dependencies.scaluzzi,
    organization := "enzief",
    scalaVersion := "2.13.3",
  )

  override val projectSettings: Seq[Def.Setting[_]] =
    headerSettings ++
      Seq(
        conflictManager := ConflictManager.strict,
        dependencyOverrides := DependencyOverrides.settings,
        autoAPIMappings in Global := true,
        addCompilerPlugin(Dependencies.CompilerPlugin.kindProjector),
        addCompilerPlugin(Dependencies.CompilerPlugin.monadicFor),
        addCompilerPlugin(scalafixSemanticdb("4.4.0")),
        scalacOptions ++= commonScalacOptions ++ optOptions ++ semanticdbOptions(target.value),
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
  private lazy val optOptions: List[String] =
    if (sys.env.contains("HELM_VERSION"))
      List(
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
        "-opt-inline-from:cats.**:scalaz.**",
        "-Yopt-inline-heuristics:everything",
        "-Yopt-log-inline"
      )
    else
      Nil

  private lazy val commonScalacOptions: List[String] =
    List(
      "-deprecation",
      "-language:existentials",
      "-language:implicitConversions",
      "-Xlint:adapted-args",
      "-Xlint:delayedinit-select",
      "-Xlint:deprecation",
      "-Xlint:doc-detached",
      "-Xlint:inaccessible",
      "-Xlint:infer-any",
      "-Xlint:missing-interpolator",
      "-Xlint:nonlocal-return",
      "-Xlint:nullary-unit",
      "-Xlint:option-implicit",
      "-Xlint:package-object-classes",
      "-Xlint:poly-implicit-overload",
      "-Xlint:private-shadow",
      "-Xlint:stars-align",
      "-Xlint:type-parameter-shadow",
      "-Xlint:unused",
      "-Xlint:valpattern",
      "-Ybackend-parallelism",
      "8",
      "-Ymacro-annotations",
      "-Yrangepos",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused:implicits,imports,locals,params,patvars,privates",
      "-Ywarn-value-discard",
      "-Yimports:java.lang,scala,scala.Predef,com.swissborg.Prelude"
    )

  private def semanticdbOptions(targetroot: File): Seq[String] =
    Seq(
      "-P:semanticdb:exclude:Macros.scala",
      "-P:semanticdb:synthetics:on",
      // otherwise semanticdb gets into META-INF and published
      s"-P:semanticdb:targetroot:${targetroot / "semanticdb"}",
    )
}
