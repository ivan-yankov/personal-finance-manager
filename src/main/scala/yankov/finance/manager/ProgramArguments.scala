package yankov.finance.manager

import yankov.args.annotations.{ProgramArgument, ProgramFlag, ProgramOption}

class ProgramArguments {
  @ProgramArgument(order = 0, defaultValue = "50")
  private var consoleLines: String = _

  @ProgramArgument(order = 1, defaultValue = "150")
  private var consoleColumns: String = _

  @ProgramArgument(order = 2, defaultValue = "income.csv")
  private var incomeFile: String = _

  @ProgramArgument(order = 3, defaultValue = "expense.csv")
  private var expenseFile: String = _

  def getConsoleLines: Int = consoleLines.toInt

  def getConsoleColumns: Int = consoleColumns.toInt

  def getIncomeFile: String = incomeFile

  def getExpenseFile: String = expenseFile
}
