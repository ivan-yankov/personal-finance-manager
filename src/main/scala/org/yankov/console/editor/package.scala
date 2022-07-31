package org.yankov.console

package object editor {
  object ConsoleColors {
    object Font {
      val reset = "\u001B[0m"
      val black = "\u001B[30m"
      val red = "\u001B[31m"
      val green = "\u001B[32m"
      val yellow = "\u001B[33m"
      val blue = "\u001B[34m"
      val purple = "\u001B[35m"
      val cyan = "\u001B[36m"
      val white = "\u001B[37m"
    }

    object Background {
      val black = "\u001B[40m"
      val red = "\u001B[41m"
      val green = "\u001B[42m"
      val yellow = "\u001B[43m"
      val blue = "\u001B[44m"
      val purple = "\u001B[45m"
      val cyan = "\u001B[46m"
      val white = "\u001B[47m"
    }
  }

  type TableRow = Seq[String]

  trait ColumnAlignment

  case object UnknownColumnAlignment extends ColumnAlignment

  case object LeftColumnAlignment extends ColumnAlignment

  case object RightColumnAlignment extends ColumnAlignment

  case class TableColumn(header: String, alignment: ColumnAlignment)

  case class Table(columns: Seq[TableColumn], rows: Seq[TableRow])
}
