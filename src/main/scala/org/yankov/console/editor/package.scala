package org.yankov.console

package object editor {
  type TableRow = Seq[String]

  trait ColumnAlignment

  case object UnknownColumnAlignment extends ColumnAlignment

  case object LeftColumnAlignment extends ColumnAlignment

  case object RightColumnAlignment extends ColumnAlignment

  case class TableColumn(header: String, alignment: ColumnAlignment)

  case class Table(columns: Seq[TableColumn], rows: Seq[TableRow])

  case class TableFocus(row: Int, col: Int)
}
