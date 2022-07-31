package org.yankov.console.editor

import Operations._

import java.io.{BufferedReader, InputStreamReader}
import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters._

object TableEditor {
  private var table: Table = _

  def main(args: Array[String]): Unit = {
    val lines = args.headOption match {
      case Some(fileName) => Files.readAllLines(Paths.get(fileName)).iterator().asScala.toSeq
      case None =>
        new BufferedReader(new InputStreamReader(getClass.getResourceAsStream("/example.csv")))
          .lines()
          .iterator()
          .asScala
          .toSeq
    }

    if (lines.isEmpty) {
      println("Empty file")
      return
    }

    parseTable(lines, ",") match {
      case Left(error) =>
        println("Unable to parse table:\n\t" + error)
      case Right(t) =>
        table = t
        println(printTable(table))
    }
  }
}
