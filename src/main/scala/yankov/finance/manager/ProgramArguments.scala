package yankov.finance.manager

import yankov.args.annotations.{ProgramArgument, ProgramFlag, ProgramOption}

import java.nio.file.{Path, Paths}

class ProgramArguments {
  @ProgramArgument(order = 0, defaultValue = "50")
  private var consoleLines: String = _

  @ProgramArgument(order = 1, defaultValue = "150")
  private var consoleColumns: String = _

  @ProgramOption(shortName = "", longName = "data-dir", defaultValue = ".")
  private var dataDir: String = _

  def getConsoleLines: Int = consoleLines.toInt

  def getConsoleColumns: Int = consoleColumns.toInt

  def getDataDir: Path = {
    if (dataDir.equals(".")) Paths.get(System.getenv("APPIMAGE")).getParent.toAbsolutePath
    else Paths.get(dataDir).toAbsolutePath
  }
}
