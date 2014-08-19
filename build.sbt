sbtPlugin := true

name := "onedir"

version := "0.6"

scalaVersion := "2.10.4"

organization := "com.ansvia"

publishMavenStyle := true

crossPaths := true

publishTo <<= version { (v: String) =>
  val nexus = "http://scala.repo.ansvia.com/nexus/"
  if (v.trim.endsWith("SNAPSHOT")) 
    Some("snapshots" at nexus + "content/repositories/snapshots") 
  else
    //Some("releases"  at nexus + "service/local/staging/deploy/maven2")
	Some("releases" at nexus + "content/repositories/releases")
}

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials-ansvia")

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>http://ansvia.com</url>
  <licenses>
    <license>
      <name>BSD-style</name>
      <url>http://www.opensource.org/licenses/bsd-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:anvie/sbt-onedir-plugin.git</url>
    <connection>scm:git:git@github.com:anvie/sbt-onedir-plugin.git</connection>
  </scm>
  <developers>
    <developer>
      <id>robin</id>
      <name>Robin Syihab</name>
      <url>http://mindtalk.com/u/robin</url>
    </developer>
  </developers>)

