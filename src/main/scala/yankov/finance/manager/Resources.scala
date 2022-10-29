package yankov.finance.manager

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, ZoneId}

object Resources {
  val tab: String = "\t"

  val zoneId: ZoneId = ZoneId.systemDefault()
  val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

  val programTitle: String = "Персонално счетоводство"
  val mainMenuTitle: String = "Меню"
  val income: String = "Приходи"
  val expense: String = "Разходи"
  val balance: String = "Баланс"
  val balanceAt: String = "Баланс към"
  val exit: String = "Изход"
  val balanceDate: String = "Дата за баланс"

  val emptyDate: LocalDate = LocalDate.MIN
}
