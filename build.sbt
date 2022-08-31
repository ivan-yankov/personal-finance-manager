lazy val root = (project in file("."))
  .settings(
    organization := "yankov",
    name := "personal-finance-manager",
    scalaVersion := "2.12.15",
    scalacOptions ++= Seq("-deprecation", "-feature"),
    version := readVersion.value(),

    resolvers += Resolver.jcenterRepo,

    Test / parallelExecution := false,

    fork := true,
    outputStrategy := Some(StdoutOutput),
    connectInput := true,
  )
