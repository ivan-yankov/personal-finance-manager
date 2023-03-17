package yankov.finance.manager

import yankov.console.ConsoleColor
import yankov.console.factory.ConsoleTableFactory
import yankov.console.model.Command
import yankov.console.operations.{ConsoleOperations, FileOperations}
import yankov.console.table.viewer.ConsoleMenu
import yankov.console.table.{Table, TableParser}
import yankov.finance.manager.Utils._
import yankov.jutils.functional.ImmutableList
import yankov.jutils.functional.tuples.Tuple

import java.nio.file.{Path, Paths}
import java.time.LocalDate
import scala.jdk.CollectionConverters._

object Menu {
  def createMenu(programArguments: ProgramArguments, consoleOperations: ConsoleOperations): ConsoleMenu = {
    def commands: List[Command] = List(
      new Command("income", _ => editIncome(programArguments, consoleOperations), Resources.income),
      new Command("expense", _ => editExpense(programArguments, consoleOperations), Resources.expense),
      new Command("balance", _ => calculateBalance(programArguments, consoleOperations), Resources.balance),
      new Command("exit", _ => exit(consoleOperations), Resources.exit)
    )

    ConsoleTableFactory.createConsoleMenu(
      ImmutableList.of(
        List(
          new Tuple(
            Resources.mainMenuTitle,
            ImmutableList.of(commands.asJava)
          )
        ).asJava),
      programArguments.getConsoleLines,
      programArguments.getConsoleColumns,
      Resources.programTitle,
      consoleOperations
    )
  }

  def editIncome(programArguments: ProgramArguments, consoleOperations: ConsoleOperations): Unit =
    editCsv(
      getIncomeFile(programArguments.getDataDir),
      programArguments.getConsoleLines,
      programArguments.getConsoleColumns,
      Resources.income,
      consoleOperations
    )

  def editExpense(programArguments: ProgramArguments, consoleOperations: ConsoleOperations): Unit =
    editCsv(
      getExpenseFile(programArguments.getDataDir),
      programArguments.getConsoleLines,
      programArguments.getConsoleColumns,
      Resources.expense,
      consoleOperations
    )

  def calculateBalance(programArguments: ProgramArguments, consoleOperations: ConsoleOperations): Unit = {
    ConsoleTableFactory.createConsoleDateSelector(
      yankov.console.Utils.firstDayOfCurrentMonth(),
      programArguments.getConsoleLines,
      programArguments.getConsoleColumns,
      date => {
        val b = calculateBalanceAtDate(date, programArguments)
        val color = if (scala.math.signum(b) < 0) ConsoleColor.RED else ConsoleColor.GREEN
        consoleOperations.clearConsole()
        println(colorText(printBalance(b, date), color))
        println()
        println("Press a key to return...")
        consoleOperations.readKey()
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
    val incomeTable = TableParser
      .fromCsv(readFile(getIncomeFile(programArguments.getDataDir)))
      .getRight
      .orElseThrow()
    val expenseTable = TableParser
      .fromCsv(readFile(getExpenseFile(programArguments.getDataDir)))
      .getRight
      .orElseThrow()
    tableTotal(incomeTable, date) - tableTotal(expenseTable, date)
  }

  def exit(consoleOperations: ConsoleOperations): Unit = {
    consoleOperations.clearConsole()
    System.exit(0)
  }

  private def editCsv(file: Path,
                      consoleLines: Int,
                      consoleColumns: Int,
                      title: String,
                      consoleOperations: ConsoleOperations): Unit = {
    ConsoleTableFactory.createConsoleTableEditor(
      file,
      consoleLines,
      consoleColumns,
      title,
      consoleOperations,
      new FileOperations(consoleOperations)
    ).getRight.orElseThrow().show()
  }

  private def getIncomeFile(dataDir: Path): Path = Paths.get(dataDir.toString, "income.csv")

  private def getExpenseFile(dataDir: Path): Path = Paths.get(dataDir.toString, "expense.csv")
}
