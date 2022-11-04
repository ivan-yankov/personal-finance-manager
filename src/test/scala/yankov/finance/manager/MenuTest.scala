package yankov.finance.manager

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate

class MenuTest extends AnyFreeSpec with Matchers {
  "calculate balance should work correct" in {
    val expectedExpense = 150.14 + 28.19 + 35.40
    val expectedIncome = 500.00 + 350.00
    val expectedBalance = expectedIncome - expectedExpense
    val date = LocalDate.of(2022, 11, 8)
    val actualBalance = Menu.calculateBalanceAtDate(date, ProgramArguments(50, 50, "income.csv", "expense.csv"))
    actualBalance - expectedBalance < 0.01 shouldBe true
  }
}
