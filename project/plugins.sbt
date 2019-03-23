import enzief.Dependencies.SbtPlugin._

dependencyOverrides := enzief.DependencyOverrides.settings

addSbtPlugin(coursier)
addSbtPlugin(dynver)
addSbtPlugin(partialUnification)
addSbtPlugin(sbtHeader)
addSbtPlugin(scalafix)
addSbtPlugin(scalafmt)
addSbtPlugin(scalastyle)
