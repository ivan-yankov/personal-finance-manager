package org.yankov.finance.manager

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Resources {
  val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
  val doubleFormat = "%.2f"

  val mainFrameTitle: String = "Персонално счетоводство"
  val wrongNumberOfArguments: String = "Wrong number of arguments. Expected: income-file expense-file"
  val incomeTitle: String = "Приходи"
  val expenseTitle: String = "Разходи"

  val insertRowBefore: String = "Вмъкване преди"
  val insertRowBeforeCommand: String = "insert-row-before"

  val insertRowAfter: String = "Вмъкване след"
  val insertRowAfterCommand: String = "insert-row-after"

  val deleteRow: String = "Изтриване"
  val deleteRowCommand: String = "delete-row"

  val save: String = "Запис"
  val saveCommand: String = "save"

  val emptyDate: LocalDate = LocalDate.MIN
  val emptyValue: Double = 0.0

  def printValue(x: Double): String = String.format(doubleFormat, x)

  def printValue(date: LocalDate): String = if (emptyDate.equals(date)) "" else date.format(dateTimeFormatter)

  def parseDouble(s: String): Double = s.toDouble

  def parseDate(s: String): LocalDate = if (s.isEmpty) emptyDate else LocalDate.parse(s, dateTimeFormatter)
}
