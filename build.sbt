import enzief.Dependencies._

enablePlugins(ProjectPlugin)

fork in Test in ThisBuild := true

resolvers in ThisBuild ++= Seq(
  Resolver.sonatypeRepo("snapshots")
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
  ";scalafmtSbt;scalafmt;test:scalafmt"
)

addCommandAlias(
  "wip",
  ";headerCreate;test:headerCreate" +
  ";fmt" +
  ";test:compile"
)

addCommandAlias(
  "check",
  ";headerCheck;test:headerCheck" +
  ";scalafmtCheck;test:scalafmtCheck;scalafmtSbtCheck" +
  ";evicted;test:evicted" +
  ";scalafix --check;test:scalafix --check" +
  ";scalastyle;test:scalastyle;"
)
