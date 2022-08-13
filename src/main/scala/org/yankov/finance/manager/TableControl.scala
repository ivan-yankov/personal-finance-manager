package org.yankov.finance.manager

import java.time.LocalDate
import javax.swing.{JTable, SwingConstants}
import javax.swing.table.{DefaultTableCellRenderer, TableCellRenderer}

class DateCellRenderer extends DefaultTableCellRenderer {
  override def setValue(value: Any): Unit = {
    setHorizontalAlignment(SwingConstants.RIGHT)
    val date = value.asInstanceOf[LocalDate]
    setText(if (date.equals(LocalDate.MIN)) "" else date.format(Resources.dateTimeFormatter))
  }
}

class DoubleCellRenderer extends DefaultTableCellRenderer {
  override def setValue(value: Any): Unit = {
    setHorizontalAlignment(SwingConstants.RIGHT)
    setText(String.format(Resources.doubleFormat, value.asInstanceOf[Double]))
  }
}

class TableControl(tableModel: FinanceManagerTableModel) extends JTable(tableModel) {
  override def getCellRenderer(row: Int, col: Int): TableCellRenderer = {
    col match {
      case 1 => new DateCellRenderer()
      case 2 => new DoubleCellRenderer()
      case _ => super.getCellRenderer(row, col)
    }
  }
}
