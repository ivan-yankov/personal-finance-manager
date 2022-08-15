package org.yankov.finance.manager

import Resources._
import com.toedter.calendar.JDateChooser
import org.yankov.finance.manager.Buttons._

import java.awt.{ComponentOrientation, FlowLayout}
import java.nio.file.{Files, Paths}
import javax.swing._
import scala.jdk.CollectionConverters._

object Main {
  val incomeTableModel: FinanceManagerTableModel = new FinanceManagerTableModel()
  val expenseTableModel: FinanceManagerTableModel = new FinanceManagerTableModel()

  var incomeTable: JTable = _
  var expenseTable: JTable = _

  val balanceDate = new JDateChooser()
  val balanceLabel = new JLabel("")

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

              val panel = new JPanel()
              panel.setLayout(flowLayout)
              panel.add(renderTable(incomeTable, incomeTitle, incomeFileName))
              panel.add(renderTable(expenseTable, expenseTitle, expenseFileName))
              panel.add(renderCalculationControls)
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

  private def flowLayout: FlowLayout = {
    val layout = new FlowLayout()
    layout.setAlignment(FlowLayout.LEFT)
    layout
  }

  private def renderCalculationControls: JPanel = {
    val panel = new JPanel()

    val layout = new GroupLayout(panel)
    panel.setLayout(layout)

    layout.setAutoCreateGaps(true)
    layout.setAutoCreateContainerGaps(true)

    val bl = new JLabel(Resources.balanceDateLabel)
    val calculateButton = button(Resources.calculate, command(calculateCommand))

    balanceDate.setDate(dateNow)

    layout.setHorizontalGroup(
      layout.createSequentialGroup()
        .addGroup(
          layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(bl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MaxValue)
            .addComponent(balanceDate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MaxValue)
            .addComponent(calculateButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MaxValue)
            .addComponent(balanceLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MaxValue)
        )
    )

    layout.setVerticalGroup(
      layout.createSequentialGroup()
        .addComponent(bl)
        .addComponent(balanceDate)
        .addComponent(calculateButton)
        .addComponent(balanceLabel)
        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
    )

    panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT)
    panel
  }

  private def tableControls(source: String, file: String): JPanel = {
    val panel = new JPanel()
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS))

    val layout = new GroupLayout(panel)
    panel.setLayout(layout)

    layout.setAutoCreateGaps(true)
    layout.setAutoCreateContainerGaps(true)

    val b1 = button(Resources.insertRowBefore, command(insertRowBeforeCommand, Seq(source)))
    val b2 = button(Resources.insertRowAfter, command(insertRowAfterCommand, Seq(source)))
    val b3 = button(Resources.deleteRow, command(deleteRowCommand, Seq(source)))
    val b4 = button(Resources.save, command(saveCommand, Seq(file, source)))

    layout.setHorizontalGroup(
      layout.createSequentialGroup()
        .addGroup(
          layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(b1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MaxValue)
            .addComponent(b2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MaxValue)
            .addComponent(b3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MaxValue)
            .addComponent(b4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MaxValue)
        )
    )

    layout.setVerticalGroup(
      layout.createSequentialGroup()
        .addComponent(b1)
        .addComponent(b2)
        .addComponent(b3)
        .addComponent(b4)
        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE))
    )

    panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT)
    panel
  }

  private def renderTable(table: JTable, title: String, file: String): JPanel = {
    val scrollPane = new JScrollPane(table)
    table.setFillsViewportHeight(true)

    val panel = new JPanel()
    panel.setLayout(flowLayout)
    panel.add(scrollPane)
    panel.add(tableControls(title, file))
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
