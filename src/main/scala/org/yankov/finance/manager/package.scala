package org.yankov.finance

import java.time.LocalDate

package object manager {
  case class TableItem(name: String, date: LocalDate, value: Double)

  case class Table(headers: Seq[String], data: Seq[TableItem])
}
