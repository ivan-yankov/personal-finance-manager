package yankov.finance.manager

import console.ConsoleColor
import console.menu.ConsoleMenu
import console.model.{Command, Pair}
import console.table.Table
import console.util.{ConsoleTableFactory, TableParser}
import yankov.finance.manager.Resources._
import yankov.finance.manager.Utils._

import java.nio.file.Paths
import java.time.LocalDate
import scala.jdk.CollectionConverters._

object Menu {
  def createMenu(programArguments: ProgramArguments): ConsoleMenu = {
    def commands: List[Command] = List(
      new Command(() => editIncome(programArguments), income),
      new Command(() => editExpense(programArguments), expense),
      new Command(() => calculateBalance(programArguments), balance),
      new Command(
        () => {
          println()
          System.exit(0)
        },
        exit
      )
    )

    ConsoleTableFactory.createConsoleMenu(
      List(
        new Pair(
          mainMenuTitle,
          commands.asJava
        )
      ).asJava,
      programArguments.consoleLines,
      programArguments.consoleColumns,
      programTitle,
      () => consoleReadLine()
    )
  }

  def editIncome(programArguments: ProgramArguments): Unit =
    editCsv(programArguments.incomeFile, programArguments.consoleLines, programArguments.consoleColumns, income)

  def editExpense(programArguments: ProgramArguments): Unit =
    editCsv(programArguments.expenseFile, programArguments.consoleLines, programArguments.consoleColumns, expense)

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

    def calculateBalanceAtDate(date: LocalDate): Double = {
      val incomeTable = TableParser.fromCsv(readFile(programArguments.incomeFile))
      val expenseTable = TableParser.fromCsv(readFile(programArguments.expenseFile))
      tableTotal(incomeTable, date) - tableTotal(expenseTable, date)
    }

    ConsoleTableFactory.createDateConsoleSelector(
      console.util.Utils.firstDayOfCurrentMonth(),
      programArguments.consoleLines,
      programArguments.consoleColumns,
      date => {
        val b = calculateBalanceAtDate(date)
        val color = if (scala.math.signum(b) < 0) ConsoleColor.RED else ConsoleColor.DARK_GREEN
        Main.getMenu.setLogMessage(colorText(printBalance(b, date), color))
      },
      () => consoleReadLine()
    ).show()
  }

  private def editCsv(file: String, consoleLines: Int, consoleColumns: Int, title: String): Unit =
    ConsoleTableFactory.createConsoleTableEditor(
      Paths.get(file),
      consoleLines,
      consoleColumns,
      title,
      () => consoleReadLine()
    ).show()
}
