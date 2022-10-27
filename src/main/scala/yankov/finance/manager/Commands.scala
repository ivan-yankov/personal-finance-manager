package yankov.finance.manager

import console.ConsoleColor
import console.editor.{ConsoleTableEditor, Table}
import console.util.TableParser
import yankov.finance.manager.Resources._
import yankov.finance.manager.Utils._

import java.nio.file.Paths
import java.time.LocalDate
import scala.annotation.tailrec

object Commands {
  def commands: Map[String, Command] = Map(
    "1" -> Command(income, editIncome),
    "2" -> Command(expense, editExpense),
    "3" -> Command(balance, calculateBalance),
    "4" -> Command(exit, _ => System.exit(0))
  )

  def emptyCommand: Command = Command("Do nothing", _ => ())

  def showMenu(): Unit = {
    @tailrec
    def processCommand(): Unit = {
      clearConsole()
      writeln(programTitle)
      writeln()
      commands.foreach(x => writeln(x._1 + ": " + x._2.description))
      writeln()

      val userInput = readLine(select)
      commands.getOrElse(userInput, emptyCommand).execute
      processCommand()
    }

    processCommand()
  }

  def editIncome(programArguments: ProgramArguments): Unit =
    editCsv(programArguments.incomeFile, programArguments.consoleLines, programArguments.consoleColumns)

  def editExpense(programArguments: ProgramArguments): Unit =
    editCsv(programArguments.expenseFile, programArguments.consoleLines, programArguments.consoleColumns)

  def calculateBalance(programArguments: ProgramArguments): Unit = {
    def tableTotal(table: Table[String], date: LocalDate): Double = {
      table
        .getData
        .map(x => TableRow(x.head, parseDate(x.tail.head), parseDouble(x.tail.tail.head)))
        .filterNot(x => x.date.equals(emptyDate))
        .filter(x => x.date.isBefore(date) || x.date.isEqual(date))
        .map(x => x.value)
        .sum
    }

    val date = parseDate(readLine(balanceDate, printDate(today)))

    val incomeTable = TableParser.fromCsv(readFile(programArguments.incomeFile))
    val expenseTable = TableParser.fromCsv(readFile(programArguments.expenseFile))
    val balance = tableTotal(incomeTable, date) - tableTotal(expenseTable, date)

    val color = if (scala.math.signum(balance) < 0) ConsoleColor.RED else ConsoleColor.GREEN
    writeln(printBalance(balance), color)
  }

  private def editCsv(file: String, consoleLines: Int, consoleColumns: Int): Unit = {
    val table = TableParser.fromCsv(readFile(file))
    new ConsoleTableEditor(table, Paths.get(file), consoleLines, consoleColumns).show()
  }
}
