package yankov.finance

import console.editor.Table

import java.time.LocalDate
import scala.jdk.CollectionConverters._

package object manager {
  case class ProgramArguments(consoleLines: Int, consoleColumns: Int, incomeFile: String, expenseFile: String)

  case class Command(description: String, execute: ProgramArguments => Unit)

  case class TableRow(description: String, date: LocalDate, value: Double)

  implicit class TableExtension[T](table: Table[T]) {
    def getData: Seq[Seq[T]] = table.getDataStream.iterator().asScala.toSeq.map(_.iterator().asScala.toSeq)
  }
}
