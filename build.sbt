import enzief.Dependencies._

fork in Test in ThisBuild := true

resolvers in ThisBuild ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)

lazy val root: Project = (project in file("."))
  .aggregate(scalaz, eg, testz)
  .enablePlugins(ProjectPlugin)
  .settings(
    skip in publish := true
  )

lazy val scalaz: Project = (project in file("scalaz"))
  .enablePlugins(ProjectPlugin)
  .settings(
    schemez(Compile, "main"),
    schemez(Test, "test"),
    name := "schemez-scalaz",
    skip in publish := false,
    publishArtifact in makePom := true,
    publishArtifact := true,
    libraryDependencies ++= Seq(
      Scalaz.core
    )
  )

def schemez(scope: Configuration, path: String): Setting[Seq[File]] =
  scope / unmanagedSourceDirectories += (
    baseDirectory
      .in(scope)
      .value / s"../schemez/src/$path/scala"
  ).getCanonicalFile

lazy val eg: Project = (project in file("example"))
  .enablePlugins(ProjectPlugin)
  .dependsOn(scalaz)
  .settings(
    name := "example",
    skip in publish := true
  )

lazy val testz: Project = (project in file("testz"))
  .enablePlugins(ProjectPlugin)
  .dependsOn(eg, scalaz)
  .settings(
    name := "testz",
    skip in publish := true,
    libraryDependencies ++= Seq(
      Scalaz.Testz.testz,
      Scalaz.laws,
      Testing.scalaCheck
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
    ";scalastyle;test:scalastyle"
)
