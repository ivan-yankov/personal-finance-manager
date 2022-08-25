package yankov.finance.manager

import com.toedter.calendar.JDateChooser

import java.awt.Component
import java.time.LocalDate
import java.util.Date
import javax.swing.table.{DefaultTableCellRenderer, TableCellEditor, TableCellRenderer}
import javax.swing.{AbstractCellEditor, JTable, SwingConstants}

class DateCellEditor extends AbstractCellEditor with TableCellEditor {
  private val dateChooser: JDateChooser = new JDateChooser()

  override def getCellEditorValue: Any = Resources.toLocalDate(dateChooser.getDate)

  override def getTableCellEditorComponent(table: JTable,
                                           value: Any,
                                           isSelected: Boolean,
                                           row: Int,
                                           column: Int): Component = {
    val localDate = value.asInstanceOf[LocalDate]

    val date = {
      if (localDate.equals(Resources.emptyDate))
        Resources.dateNow
      else
        Date.from(localDate.atStartOfDay(Resources.zoneId).toInstant)
    }

    dateChooser.setDate(date)
    dateChooser
  }
}

class DateCellRenderer extends DefaultTableCellRenderer {
  override def setValue(value: Any): Unit = {
    setHorizontalAlignment(SwingConstants.RIGHT)
    setText(Resources.printValue(value.asInstanceOf[LocalDate]))
  }
}

class DoubleCellRenderer extends DefaultTableCellRenderer {
  override def setValue(value: Any): Unit = {
    setHorizontalAlignment(SwingConstants.RIGHT)
    setText(Resources.printValue(value.asInstanceOf[Double]))
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

  override def getCellEditor(row: Int, col: Int): TableCellEditor = {
    col match {
      case 1 => new DateCellEditor()
      case _ => super.getCellEditor(row, col)
    }
  }
}