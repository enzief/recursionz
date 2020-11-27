import enzief.Dependencies.SbtPlugin._

dependencyOverrides := enzief.DependencyOverrides.settings

addSbtPlugin(dynver)
addSbtPlugin(sbtHeader)
addSbtPlugin(scalafix)
addSbtPlugin(scalafmt)
