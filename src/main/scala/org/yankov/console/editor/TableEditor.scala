package org.yankov.console.editor

import Operations._

import java.io.{BufferedReader, InputStreamReader}
import java.nio.file.{Files, Paths}
import scala.annotation.tailrec
import scala.jdk.CollectionConverters._
import scala.io.AnsiColor._
import scala.io.StdIn._

object TableEditor {
  private var table: Table = _
  private var tableFocus: TableFocus = TableFocus(0, 0)

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

    renderAvailableCommands()
    renderTable(lines)

    @tailrec
    def waitForCommand(): Unit = {
      println()
      print("Command: ")
      readChar() match {
        case 'q' => sys.exit()
        case _ => waitForCommand()
      }
    }

    waitForCommand()
  }

  private def renderAvailableCommands(): Unit = {
    val s = List(
      "Available commands:",
      "  q - quit"
    ).map(x => WHITE_B + x + RESET).mkString("\n")

    println(s)
    println()
  }

  private def renderTable(lines: Seq[String]): Unit = {
    parseTable(lines, ",") match {
      case Left(error) =>
        println("Unable to parse table:\n\t" + error)
      case Right(t) =>
        table = t
        println(printTable(table, tableFocus))
    }
  }
}
