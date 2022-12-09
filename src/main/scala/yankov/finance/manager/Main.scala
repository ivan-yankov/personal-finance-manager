package yankov.finance.manager

import yankov.args.ProgramArgumentsParser
import yankov.console.operations.ConsoleOperations
import yankov.console.table.viewer.ConsoleMenu

object Main {
  private var menu: ConsoleMenu = _

  private val consoleOperations = new ConsoleOperations()

  private val programArguments = new ProgramArguments()

  def main(args: Array[String]): Unit = {
    ProgramArgumentsParser.parse(args, programArguments)

    menu = Menu.createMenu(programArguments, consoleOperations)
    menu.show()

    Menu.exit(consoleOperations)
  }

  def getMenu: ConsoleMenu = menu
}
