package yankov.finance

import yankov.console.table.{Cell, Table}

import java.time.LocalDate
import scala.jdk.CollectionConverters._

package object manager {
  case class TableRow(description: String, date: LocalDate, value: Double)

  implicit class TableExtension[T](table: Table[T]) {
    def getDataAsScala: Seq[Seq[Cell[T]]] = table.getData.iterator().asScala.toSeq.map(_.iterator().asScala.toSeq)
  }
}
