import java.nio.file.{Files, Path, Paths}
import scala.collection.JavaConverters._

name := "table-editor"

version := "0.1"

scalaVersion := "2.13.8"

val collectDependencies = taskKey[Unit]("collect dependencies")
collectDependencies := {
  val jarDir = Paths.get("jar")
  if (!Files.exists(jarDir)) Files.createDirectories(jarDir)

  println("Collect dependencies")

  def collectJarsFromDir(files: Seq[Path]): Unit = {
    files.filter(x => x.toString.endsWith(".jar"))
      .foreach(
        x => {
          val src = x
          val dest = Paths.get(jarDir.toString, x.getFileName.toString)
          Files.copy(src, dest)
        }
      )
  }

  val cp: Seq[File] = (fullClasspath in Runtime).value.files
  collectJarsFromDir(cp.map(x => x.toPath))

  collectJarsFromDir(Files.walk(Paths.get("target", "scala-2.13"), 1).iterator().asScala.toList)
}
