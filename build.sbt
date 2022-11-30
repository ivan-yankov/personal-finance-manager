import java.nio.file.Paths

val scalaTestVersion = "3.2.14"
val consoleUtilsVersion = "latest"

lazy val root = (project in file("."))
  .settings(
    organization := "yankov",
    name := "personal-finance-manager",
    scalaVersion := "2.13.8",
    scalacOptions ++= Seq("-deprecation", "-feature"),
    version := readVersion.value(),
    isSnapshot := true,

    targetDir := Paths.get("target", "scala-2.13"),

    resolvers += Resolver.mavenLocal,
    resolvers += Resolver.jcenterRepo,

    Test / parallelExecution := false,

    libraryDependencies ++= Seq(
      "yankov" % "console-utils" % consoleUtilsVersion,
      "org.scalatest" %% "scalatest" % scalaTestVersion % Test
    ),

    fork := true,
    outputStrategy := Some(StdoutOutput),
    connectInput := true,
  )
