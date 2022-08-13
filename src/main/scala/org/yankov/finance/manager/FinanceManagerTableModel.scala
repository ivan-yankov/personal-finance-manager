package org.yankov.finance.manager

import java.time.LocalDate
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

class FinanceManagerTableModel() extends TableModel {
  private var table: Table = _

  def getTable: Table = table

  def setTable(table: Table): Unit = this.table = table

  private def getField(item: TableItem, col: Int): Any = {
    col match {
      case 0 => item.name
      case 1 => item.date
      case 2 => item.value
      case _ => ""
    }
  }

  private def setField(item: TableItem, col: Int, value: Any): TableItem = {
    col match {
      case 0 => TableItem(value.toString, item.date, item.value)
      case 1 => TableItem(item.name, value.asInstanceOf[LocalDate], item.value)
      case 2 => TableItem(item.name, item.date, value.asInstanceOf[Double])
      case _ => item
    }
  }

  override def getRowCount: Int = getTable.data.size

  override def getColumnCount: Int = getTable.headers.size

  override def getColumnName(c: Int): String = getTable.headers(c)

  override def getColumnClass(c: Int): Class[_] = getValueAt(0, c).getClass

  override def isCellEditable(row: Int, col: Int): Boolean = true

  override def getValueAt(row: Int, col: Int): Any = getField(getTable.data(row), col)

  override def setValueAt(value: Any, row: Int, col: Int): Unit = {
    val updatedData = table.data.zipWithIndex.map(x => if (x._2 == row) setField(x._1, col, value) else x._1)
    table = Table(table.headers, updatedData)
  }

  override def addTableModelListener(tableModelListener: TableModelListener): Unit = {}

  override def removeTableModelListener(tableModelListener: TableModelListener): Unit = {}
}
