package org.yankov.finance.manager

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JButton, JTable}

object Buttons extends ActionListener {
  def button(title: String, command: String): JButton = {
    val b = new JButton(title)
    b.setActionCommand(command)
    b.addActionListener(this)
    b
  }

  def command(cmd: String, source: String): String = cmd + ":" + source

  override def actionPerformed(actionEvent: ActionEvent): Unit = {
    val cmd = actionEvent.getActionCommand
    val tbl = sourceTable(cmd)
    if (cmd.startsWith(Resources.insertRowBeforeCommand))
      tbl.getModel.asInstanceOf[FinanceManagerTableModel].insertRowBefore(tbl.getSelectedRow)
    else if (cmd.startsWith(Resources.insertRowAfterCommand))
      tbl.getModel.asInstanceOf[FinanceManagerTableModel].insertRowAfter(tbl.getSelectedRow)
    else if (cmd.startsWith(Resources.deleteRowCommand))
      tbl.getModel.asInstanceOf[FinanceManagerTableModel].deleteRow(tbl.getSelectedRow)
    tbl.repaint()
  }

  private def sourceTable(cmd: String): JTable = {
    if (cmd.endsWith(Resources.incomeTitle)) Main.incomeTable
    else Main.expenseTable
  }
}
