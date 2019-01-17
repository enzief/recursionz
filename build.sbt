import enzief.Dependencies._

fork in Test in ThisBuild := true

resolvers in ThisBuild ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)

lazy val root = (project in file("."))
  .aggregate(schemez, eg)
  .enablePlugins(ProjectPlugin)
  .settings(
    skip in publish := true
  )

lazy val schemez: Project = (project in file("schemez"))
  .enablePlugins(ProjectPlugin)
  .settings(
    name                       := "schemez",
    skip in publish            := false,
    publishArtifact in makePom := true,
    publishArtifact            := true,
    libraryDependencies ++= Seq(
      Scalaz.core,
      Testing.scalaCheck % Test
    )
  )

lazy val eg: Project = (project in file("example"))
  .enablePlugins(ProjectPlugin)
  .dependsOn(schemez)
  .settings(
    name            := "example",
    skip in publish := true
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
  ";scalastyle;test:scalastyle"
)
