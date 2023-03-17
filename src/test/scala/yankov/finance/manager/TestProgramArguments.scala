package yankov.finance.manager

import java.nio.file.Path

case class TestProgramArguments(lines: Int, cols: Int, dataDir: Path) extends ProgramArguments {
  override def getConsoleLines: Int = lines

  override def getConsoleColumns: Int = cols

  override def getDataDir: Path = dataDir
}
