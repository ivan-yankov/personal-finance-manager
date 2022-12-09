package yankov.finance.manager

case class TestProgramArguments(lines: Int, cols: Int, income: String, expense: String) extends ProgramArguments {
  override def getConsoleLines: Int = lines

  override def getConsoleColumns: Int = cols

  override def getIncomeFile: String = income

  override def getExpenseFile: String = expense
}
