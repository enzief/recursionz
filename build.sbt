import enzief.Dependencies._

enablePlugins(ProjectPlugin)

fork in Test in ThisBuild := true

resolvers in ThisBuild ++= Seq(
  Resolver.bintrayRepo("rbmhtechnology", "maven")
)

skip in publish in ThisBuild := true

lazy val recursionz: Project = (project in file("."))
  .enablePlugins(ProjectPlugin)
  .settings(
    name                       := "recursionz",
    skip in publish            := false,
    publishArtifact in makePom := true,
    publishArtifact            := true,
    libraryDependencies ++= Seq(
      Scalaz.core,
      Testing.scalaCheck % Test
    )
  )

addCommandAlias(
  "fmt",
  ";scalafmtSbt;scalafmt;test:scalafmt;it:scalafmt"
)

addCommandAlias(
  "wip",
  ";headerCreate;test:headerCreate;it:headerCreate" +
  ";fmt" +
  ";test:compile;it:compile"
)

addCommandAlias(
  "check",
  ";headerCheck;test:headerCheck;it:headerCheck" +
  ";scalafmtCheck;test:scalafmtCheck;scalafmtSbtCheck;it:scalafmtCheck" +
  ";evicted;test:evicted;it:evicted" +
  ";scalafix --check;test:scalafix --check;it:scalafix --check" +
  ";scalastyle;test:scalastyle;it:scalastyle"
)
