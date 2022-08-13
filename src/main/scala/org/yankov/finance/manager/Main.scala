package org.yankov.finance.manager

import Resources._
import org.yankov.finance.manager.Buttons._

import java.awt.{ComponentOrientation, FlowLayout}
import java.nio.file.{Files, Paths}
import javax.swing._
import scala.jdk.CollectionConverters._

object Main {
  private val incomeTableModel: FinanceManagerTableModel = new FinanceManagerTableModel()
  private val expenseTableModel: FinanceManagerTableModel = new FinanceManagerTableModel()

  var incomeTable: JTable = _
  var expenseTable: JTable = _

  def main(args: Array[String]): Unit = {
    val tablesContainer: Option[JPanel] = args.headOption match {
      case Some(incomeFileName) =>
        val incomeLines = readFile(incomeFileName)
        val incomeJTable = createTable(incomeLines, incomeTableModel)
        args.tail.headOption match {
          case Some(expenseFileName) =>
            val expenseLines = readFile(expenseFileName)
            val expenseJTable = createTable(expenseLines, expenseTableModel)
            if (incomeJTable.isDefined && expenseJTable.isDefined) {
              incomeTable = incomeJTable.get
              expenseTable = expenseJTable.get

              val layout = new FlowLayout()
              layout.setAlignment(FlowLayout.LEFT)
              val panel = new JPanel()
              panel.setLayout(layout)
              panel.add(renderTable(incomeTable, incomeTitle, incomeFileName))
              panel.add(renderTable(expenseTable, expenseTitle, expenseFileName))
              panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT)
              Some(panel)
            }
            else None
          case None =>
            logError(wrongNumberOfArguments)
            None
        }
      case None =>
        logError(wrongNumberOfArguments)
        None
    }

    val frame = new JFrame(mainFrameTitle)
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setSize(1500, 500)
    if (tablesContainer.isDefined) frame.setContentPane(tablesContainer.get)
    frame.setVisible(true)
  }

  private def operationControls(source: String, file: String): JPanel = {
    val panel = new JPanel()
    val layout = new BoxLayout(panel, BoxLayout.Y_AXIS)
    panel.setLayout(layout)
    panel.add(button(Resources.insertRowBefore, command(insertRowBeforeCommand, Seq(source))))
    panel.add(button(Resources.insertRowAfter, command(insertRowAfterCommand, Seq(source))))
    panel.add(button(Resources.deleteRow, command(deleteRowCommand, Seq(source))))
    panel.add(button(Resources.save, command(saveCommand, Seq(file, source))))
    panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT)
    panel
  }

  private def renderTable(table: JTable, title: String, file: String): JPanel = {
    val scrollPane = new JScrollPane(table)
    table.setFillsViewportHeight(true)

    val layout = new FlowLayout()
    layout.setAlignment(FlowLayout.LEFT)
    val panel = new JPanel()
    panel.setLayout(layout)
    panel.add(scrollPane)
    panel.add(operationControls(title, file))
    panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT)
    panel.setBorder(BorderFactory.createTitledBorder(title))
    panel
  }

  private def createTable(lines: Seq[String], tableModel: FinanceManagerTableModel): Option[JTable] = {
    parseTable(lines, ",") match {
      case Left(error) =>
        logError("Unable to parse table:\n\t" + error)
        None
      case Right(table) =>
        tableModel.setTable(table)
        Some(new TableControl(tableModel))
    }
  }

  def parseTable(lines: Seq[String], columnSeparator: String): Either[String, Table] = {
    val headers = lines.head.split(columnSeparator)

    def item(columns: Array[String]): Option[TableItem] = {
      if (columns.length == headers.size) Some(
        TableItem(
          name = columns(0),
          date = parseDate(columns(1)),
          value = parseDouble(columns(2))
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

  private def readFile(fileName: String): Seq[String] = Files.readAllLines(Paths.get(fileName)).iterator().asScala.toSeq

  private def logError(message: String): Unit = System.err.println(message)
}
