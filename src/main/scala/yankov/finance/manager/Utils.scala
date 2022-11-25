package yankov.finance.manager

import yankov.console.ConsoleColor
import yankov.finance.manager.Resources._

import java.nio.file.{Files, Paths}
import java.time.{Instant, LocalDate}
import java.util.Date

object Utils {
  def writeln(s: String = "", color: String = ""): Unit = {
    if (color.nonEmpty) println(colorText(s, color))
    else println(s)
  }

  def colorText(s: String, color: String): String = color + s + ConsoleColor.RESET

  def readLine(message: String, defaultValue: String = ""): String = {
    val dv = if (defaultValue.nonEmpty) " [" + defaultValue + "]" else ""
    val input = scala.io.StdIn.readLine(s"$message$dv: ")
    if (input.isEmpty && defaultValue.nonEmpty) defaultValue else input
  }

  def consoleReadLine: () => String = scala.io.StdIn.readLine

  def writeError(s: String): Unit = System.err.println(s)

  def readFile(fileName: String): String = Files.readString(Paths.get(fileName))

  def printBalance(value: Double, date: LocalDate): String = s"$balanceAt ${printDate(date)}: %.2f".format(value)

  def parseDouble(s: String): Double = s.toDouble

  def parseDate(s: String): LocalDate = if (s.isEmpty) emptyDate else LocalDate.parse(s, dateFormatter)

  def printDate(date: LocalDate): String = if (date.equals(emptyDate)) "" else date.format(dateFormatter)

  def toLocalDate(date: Date): LocalDate = Instant.ofEpochMilli(date.getTime).atZone(Resources.zoneId).toLocalDate

  def today: LocalDate = toLocalDate(instantNow)

  def clearConsole(): Unit = {
    try {
      val os = System.getProperty("os.name")
      val pb = {
        if (os.contains("Windows")) new ProcessBuilder("cmd", "/c", "cls")
        else new ProcessBuilder("clear")
      }
      val p = pb.inheritIO.start
      p.waitFor
    } catch {
      case e: Exception => writeError("Unable to clear the console: " + e.getMessage)
    }
  }

  private def instantNow: Date = Date.from(LocalDate.now(zoneId).atStartOfDay(zoneId).toInstant)
}
