package org.yankov.finance.manager

import Operations._
import Resources._
import org.yankov.finance.manager.Buttons.button

import java.awt.{ComponentOrientation, FlowLayout}
import java.nio.file.{Files, Paths}
import javax.swing._
import scala.jdk.CollectionConverters._

object Main {
  private val tableModel = new FinanceManagerTableModel()

  def main(args: Array[String]): Unit = {
    val tablesContainer: Option[JPanel] = args.headOption match {
      case Some(incomeFileName) =>
        val incomeLines = readFile(incomeFileName)
        val incomeTable = createTable(incomeLines)
        args.tail.headOption match {
          case Some(expenseFileName) =>
            val expenseLines = readFile(expenseFileName)
            val expenseTable = createTable(expenseLines)
            if (incomeTable.isDefined && expenseTable.isDefined) {
              val layout = new FlowLayout()
              layout.setAlignment(FlowLayout.LEFT)
              val panel = new JPanel()
              panel.setLayout(layout)
              panel.add(renderTable(incomeTable.get, incomeTitle))
              panel.add(renderTable(expenseTable.get, expenseTitle))
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

  private def operationControls: JPanel = {
    val panel = new JPanel()
    val layout = new BoxLayout(panel, BoxLayout.Y_AXIS)
    panel.setLayout(layout)
    panel.add(button(addRow, addRowCommand))
    panel.add(button(insertRow, insertRowCommand))
    panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT)
    panel
  }

  private def renderTable(table: JTable, title: String): JPanel = {
    val scrollPane = new JScrollPane(table)
    table.setFillsViewportHeight(true)

    val layout = new FlowLayout()
    layout.setAlignment(FlowLayout.LEFT)
    val panel = new JPanel()
    panel.setLayout(layout)
    panel.add(scrollPane)
    panel.add(operationControls)
    panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT)
    panel.setBorder(BorderFactory.createTitledBorder(title))
    panel
  }

  private def createTable(lines: Seq[String]): Option[JTable] = {
    parseTable(lines, ",") match {
      case Left(error) =>
        logError("Unable to parse table:\n\t" + error)
        None
      case Right(table) =>
        tableModel.setTable(table)
        Some(new JTable(tableModel))
    }
  }

  private def readFile(fileName: String): Seq[String] = Files.readAllLines(Paths.get(fileName)).iterator().asScala.toSeq

  private def logError(message: String): Unit = System.err.println(message)
}
