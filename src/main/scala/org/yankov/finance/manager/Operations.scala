package org.yankov.finance.manager

import scala.io.AnsiColor._

object Operations {
  def insertRowBefore(table: Table, index: Int): Table = {
    if (table.rows.isEmpty) Table(table.columns, Seq(createRow(table.columns.size)))
    else if (index >= table.rows.size) table
    else Table(
      table.columns,
      table.rows.take(index) ++ Seq(createRow(table.columns.size)) ++ table.rows.takeRight(table.rows.size - index)
    )
  }

  def insertRowAfter(table: Table, index: Int): Table = {
    if (table.rows.isEmpty) Table(table.columns, Seq(createRow(table.columns.size)))
    else if (index >= table.rows.size) table
    else Table(
      table.columns,
      table.rows.take(index + 1)
        ++ Seq(createRow(table.columns.size))
        ++ table.rows.takeRight(table.rows.size - index - 1)
    )
  }

  def deleteRow(table: Table, index: Int): Table = {
    if (table.rows.isEmpty || index >= table.rows.size) table
    else Table(table.columns, table.rows.take(index) ++ table.rows.takeRight(table.rows.size - index - 1))
  }

  def updateField(table: Table, row: Int, col: Int, newValue: String): Table = {
    def updateValue(tableRow: TableRow): TableRow = {
      tableRow.zipWithIndex.map(x => if (x._2 == col) newValue else x._1)
    }

    if (table.rows.isEmpty || row >= table.rows.size || col >= table.columns.size) table
    else Table(
      table.columns,
      table.rows.zipWithIndex.map(x => if (x._2 == row) updateValue(x._1) else x._1)
    )
  }

  def printTable(table: Table, tableFocus: TableFocus): String = {
    val columnDelimiter = " | "
    val columnsWithSizes = table
      .columns
      .zipWithIndex
      .map(
        x =>
          (
            x._1, // column
            x._2, // column index
            getColumn(table, x._2).map(y => y.length).max, // field size
            tableFocus.col == x._2 // focused
          )
      )

    val header = columnsWithSizes
      .map(x => printField(x._1.header, x._1.alignment, x._3, x._4))
      .mkString(columnDelimiter)

    val headerBorder = columnsWithSizes.map(x => Seq.fill(x._3)("-").mkString).mkString(columnDelimiter)

    val rows = table
      .rows
      .zipWithIndex
      .map(
        rowWithIndex => rowWithIndex._1
          .zip(columnsWithSizes)
          .map(y => printField(y._1, y._2._1.alignment, y._2._3, y._2._4 && rowWithIndex._2 == tableFocus.row))
          .mkString(columnDelimiter)
      )

    (headerBorder +: header +: headerBorder +: rows).mkString("\n")
  }

  def parseTable(lines: Seq[String], columnSeparator: String): Either[String, Table] = {
    val table = Table(
      lines.head.split(columnSeparator).map(x => TableColumn(x, UnknownColumnAlignment)),
      lines.tail.map(x => x.split(columnSeparator))
    )
    if (table.rows.forall(x => x.size == table.columns.size)) Right(table)
    else {
      val wrongLineNumbers = table.rows.zipWithIndex.filter(x => x._1.size != table.columns.size).map(x => x._2)
      Left(s"Column count mismatch at lines [${wrongLineNumbers.mkString(",")}]")
    }
  }

  def printField(s: String, alignment: ColumnAlignment, fieldSize: Int, focused: Boolean): String = {
    if (s.length >= fieldSize) return s

    def detectAlignment: ColumnAlignment = {
      if (s.forall(x => x.isDigit || x.equals('.'))) RightColumnAlignment
      else LeftColumnAlignment
    }

    val a = if (alignment == UnknownColumnAlignment) detectAlignment else alignment

    val spaces = Seq.fill(fieldSize - s.length)(" ").mkString

    val r = a match {
      case LeftColumnAlignment => s + spaces
      case RightColumnAlignment => spaces + s
      case _ => s
    }

    if (focused) BLUE_B + r + RESET
    else r
  }

  private def createRow(size: Int): TableRow = Seq.fill(size)("")

  private def getColumn(table: Table, index: Int): Seq[String] =
    table.columns.slice(index, index + 1).head.header +: table.rows.map(x => x.slice(index, index + 1).head)
}
