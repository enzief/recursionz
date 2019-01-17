managedSources in Compile ++= Seq(
  baseDirectory.value / ".." / "Dependencies.scala",
  baseDirectory.value / ".." / "DependencyOverrides.scala"
)
