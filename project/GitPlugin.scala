package enzief

import scala.sys.process._
import scala.util.Try

object GitPlugin {

  def buildDockerVersion: String =
    Try("docker --version".!!.trim).getOrElse("n/a")

  def buildHost: String = Try("hostname".!!.trim).getOrElse("n/a")

  def buildUser: String = Try("whoami".!!.trim).getOrElse("n/a")

  def gitBranch: String =
    Try("git symbolic-ref --short -q HEAD".!!.trim).getOrElse("n/a")

  def gitCommitHash: String =
    Try("git rev-parse --verify HEAD".!!.trim).getOrElse("n/a")

  def gitRepository: String =
    Try("git ls-remote --get-url".!!.trim).getOrElse("n/a")
}
