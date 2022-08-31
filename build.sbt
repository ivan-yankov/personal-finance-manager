import java.nio.file.Paths

lazy val root = (project in file("."))
  .settings(
    organization := "yankov",
    name := "personal-finance-manager",
    scalaVersion := "2.13.8",
    scalacOptions ++= Seq("-deprecation", "-feature"),
    version := readVersion.value(),

    targetDir := Paths.get("target", "scala-2.13"),

    resolvers += Resolver.jcenterRepo,

    Test / parallelExecution := false,

    fork := true,
    outputStrategy := Some(StdoutOutput),
    connectInput := true,
  )
