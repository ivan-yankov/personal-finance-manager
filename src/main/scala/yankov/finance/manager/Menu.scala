package yankov.finance.manager

import console.ConsoleColor
import console.factory.ConsoleTableFactory
import console.model.{Command, Pair}
import console.operations.{ConsoleOperations, FileOperations}
import console.table.{ConsoleMenu, Table, TableParser}
import yankov.finance.manager.Resources._
import yankov.finance.manager.Utils._

import java.nio.file.Paths
import java.time.LocalDate
import scala.jdk.CollectionConverters._

object Menu {
  private val consoleOperations = new ConsoleOperations()

  def createMenu(programArguments: ProgramArguments): ConsoleMenu = {
    def commands: List[Command] = List(
      new Command("income", () => editIncome(programArguments), income),
      new Command("expense", () => editExpense(programArguments), expense),
      new Command("balance", () => calculateBalance(programArguments), balance),
      new Command(
        "exit",
        () => {
          consoleOperations.clearConsole()
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
      consoleOperations
    )
  }

  def editIncome(programArguments: ProgramArguments): Unit =
    editCsv(programArguments.incomeFile, programArguments.consoleLines, programArguments.consoleColumns, income)

  def editExpense(programArguments: ProgramArguments): Unit =
    editCsv(programArguments.expenseFile, programArguments.consoleLines, programArguments.consoleColumns, expense)

  def calculateBalance(programArguments: ProgramArguments): Unit = {
    ConsoleTableFactory.createDateConsoleSelector(
      console.Utils.firstDayOfCurrentMonth(),
      programArguments.consoleLines,
      programArguments.consoleColumns,
      date => {
        val b = calculateBalanceAtDate(date, programArguments)
        val color = if (scala.math.signum(b) < 0) ConsoleColor.RED else ConsoleColor.GREEN
        Main.getMenu.setLogMessage(colorText(printBalance(b, date), color))
      },
      consoleOperations
    ).show()
  }

  def tableTotal(table: Table[String], date: LocalDate): Double = {
    table
      .getDataAsScala
      .filterNot(x => List(x.tail.head, x.tail.tail.head).map(_.getValue).contains(""))
      .map(x => TableRow(x.head.getValue, parseDate(x.tail.head.getValue), parseDouble(x.tail.tail.head.getValue)))
      .filter(x => x.date.isBefore(date) || x.date.isEqual(date))
      .map(x => x.value)
      .sum
  }

  def calculateBalanceAtDate(date: LocalDate, programArguments: ProgramArguments): Double = {
    val incomeTable = TableParser.fromCsv(readFile(programArguments.incomeFile))
    val expenseTable = TableParser.fromCsv(readFile(programArguments.expenseFile))
    tableTotal(incomeTable, date) - tableTotal(expenseTable, date)
  }

  private def editCsv(file: String, consoleLines: Int, consoleColumns: Int, title: String): Unit =
    ConsoleTableFactory.createConsoleTableEditor(
      Paths.get(file),
      consoleLines,
      consoleColumns,
      title,
      consoleOperations,
      new FileOperations(consoleOperations)
    ).show()
}
