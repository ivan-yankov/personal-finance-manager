package org.yankov.finance.manager

import java.awt.Color
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JButton, JTable}

object Buttons extends ActionListener {
  private val cmdSeparator = ":"

  def button(title: String, command: String): JButton = {
    val b = new JButton(title)
    b.setActionCommand(command)
    b.addActionListener(this)
    b
  }

  def command(cmd: String, parameters: Seq[String] = Seq()): String =
    cmd + cmdSeparator + parameters.mkString(cmdSeparator)

  override def actionPerformed(actionEvent: ActionEvent): Unit = {
    val cmd = actionEvent.getActionCommand
    val tbl = sourceTable(cmd)
    if (cmd.startsWith(Resources.insertRowBeforeCommand))
      tbl.getModel.asInstanceOf[FinanceManagerTableModel].insertRowBefore(tbl.getSelectedRow)
    else if (cmd.startsWith(Resources.insertRowAfterCommand))
      tbl.getModel.asInstanceOf[FinanceManagerTableModel].insertRowAfter(tbl.getSelectedRow)
    else if (cmd.startsWith(Resources.deleteRowCommand))
      tbl.getModel.asInstanceOf[FinanceManagerTableModel].deleteRow(tbl.getSelectedRow)
    else if (cmd.startsWith(Resources.saveCommand))
      tbl.getModel.asInstanceOf[FinanceManagerTableModel].save(tableFile(cmd))
    else if (cmd.startsWith(Resources.calculateCommand)) {
      val balance = calculateBalance
      val color = if (scala.math.signum(balance) < 0) Color.RED else Color.GREEN
      Main.balanceLabel.setText(Resources.balanceLabel(balance))
      Main.balanceLabel.setForeground(color)
    }

    tbl.repaint()
  }

  private def calculateBalance: Double = {
    def tableTotal(table: Table): Double =
      table.data.filterNot(x => x.date.equals(Resources.emptyDate)).map(x => x.value).sum

    tableTotal(Main.incomeTableModel.getTable) - tableTotal(Main.expenseTableModel.getTable)
  }

  private def sourceTable(cmd: String): JTable = {
    if (cmd.endsWith(Resources.incomeTitle)) Main.incomeTable
    else Main.expenseTable
  }

  private def tableFile(cmd: String): String = cmd.split(cmdSeparator).tail.head
}
