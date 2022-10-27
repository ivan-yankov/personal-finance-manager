package yankov.finance.manager

import Resources._
import Utils._

object Main {
  def main(args: Array[String]): Unit = {
    val numberOfRequiredArguments = 4
    if (args.length < numberOfRequiredArguments) {
      writeError(s"Missing required argument. Required $numberOfRequiredArguments provided " + args.length)
      writeln("Program arguments:")
      writeln(tab + "number-of-console-lines [required]: Number of lines of the console")
      writeln(tab + "number-of-console-columns [required]: Number of columns of the console")
      writeln(tab + "income-file [required]: CSV file with income data")
      writeln(tab + "expense-file [required]: CSV file with expense data")
      return
    }

    Commands.showMenu()
  }
}
