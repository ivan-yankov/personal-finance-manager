package org.yankov.finance.manager

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.io.AnsiColor._

object Operations {
//  def insertRowBefore(table: Table, index: Int): Table = {
//    if (table.rows.isEmpty) Table(table.columns, Seq(createRow(table.columns.size)))
//    else if (index >= table.rows.size) table
//    else Table(
//      table.columns,
//      table.rows.take(index) ++ Seq(createRow(table.columns.size)) ++ table.rows.takeRight(table.rows.size - index)
//    )
//  }

//  def insertRowAfter(table: Table, index: Int): Table = {
//    if (table.rows.isEmpty) Table(table.columns, Seq(createRow(table.columns.size)))
//    else if (index >= table.rows.size) table
//    else Table(
//      table.columns,
//      table.rows.take(index + 1)
//        ++ Seq(createRow(table.columns.size))
//        ++ table.rows.takeRight(table.rows.size - index - 1)
//    )
//  }

//  def deleteRow(table: Table, index: Int): Table = {
//    if (table.rows.isEmpty || index >= table.rows.size) table
//    else Table(table.columns, table.rows.take(index) ++ table.rows.takeRight(table.rows.size - index - 1))
//  }

//  def updateField(table: Table, row: Int, col: Int, newValue: String): Table = {
//    def updateValue(tableRow: TableRow): TableRow = {
//      tableRow.zipWithIndex.map(x => if (x._2 == col) newValue else x._1)
//    }
//
//    if (table.rows.isEmpty || row >= table.rows.size || col >= table.columns.size) table
//    else Table(
//      table.columns,
//      table.rows.zipWithIndex.map(x => if (x._2 == row) updateValue(x._1) else x._1)
//    )
//  }

  def parseTable(lines: Seq[String], columnSeparator: String): Either[String, Table] = {
    val headers = lines.head.split(columnSeparator)

    def item(columns: Array[String]): Option[TableItem] = {
      if (columns.length == headers.size) Some(
        TableItem(
          name = columns(0),
          date = LocalDate.parse(columns(1), DateTimeFormatter.ofPattern("dd.MM.yyyy")),
          value = columns(2).toInt
        )
      )
      else None
    }

    val data = lines.tail.map(x => item(x.split(columnSeparator)))
    if (data.forall(x => x.isDefined)) Right(Table(headers, data.map(_.get)))
    else {
      val wrongLineNumbers = data.zipWithIndex.filter(x => x._1.size != headers.size).map(x => x._2)
      Left(s"Column count mismatch at lines [${wrongLineNumbers.mkString(",")}]")
    }
  }

//  def printField(s: String, alignment: ColumnAlignment, fieldSize: Int, focused: Boolean): String = {
//    if (s.length >= fieldSize) return s
//
//    def detectAlignment: ColumnAlignment = {
//      if (s.forall(x => x.isDigit || x.equals('.'))) RightColumnAlignment
//      else LeftColumnAlignment
//    }
//
//    val a = if (alignment == UnknownColumnAlignment) detectAlignment else alignment
//
//    val spaces = Seq.fill(fieldSize - s.length)(" ").mkString
//
//    val r = a match {
//      case LeftColumnAlignment => s + spaces
//      case RightColumnAlignment => spaces + s
//      case _ => s
//    }
//
//    if (focused) BLUE_B + r + RESET
//    else r
//  }

//  private def createRow(size: Int): TableRow = Seq.fill(size)("")

//  private def getColumn(table: Table, index: Int): Seq[String] =
//    table.columns.slice(index, index + 1).head.header +: table.rows.map(x => x.slice(index, index + 1).head)
}
