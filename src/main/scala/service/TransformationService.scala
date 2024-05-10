package service

import cats.Foldable
import types.{Batch, OutputDataFrame}

import scala.annotation.tailrec

trait TransformationService extends ConvolutionService {

  def transform(input: Batch, coreSize: Int): Either[String, OutputDataFrame] = {
    val rowsGroups = input.sliding(coreSize, 1).toVector
    val output = Foldable[Seq].foldLeft((0 until input.size - 2).toList, Seq.empty[Seq[Double]]) {
      case (output, index) => output :+ calculateValuesFromRowsGroup(rowsGroups(index), coreSize)
    }
    OutputDataFrame(output)
  }

  private def calculateValuesFromRowsGroup(rows: Seq[Seq[Int]], coreSize: Int): Seq[Double] = {
    @tailrec
    def calculateHelp(begin: Int, output: Seq[Double]): Seq[Double] = {
      val parts = for {
        row <- rows
        rowPart <- Seq(row.slice(begin, coreSize + begin))
      } yield rowPart
      val valuesFromCore = parts.reduce(_ :++ _)
      val valuesFromCoreWithoutCentralEl = valuesFromCore.take(valuesFromCore.length / 2) :++ valuesFromCore.takeRight(valuesFromCore.length / 2)
      val updatedElement = foldElements(valuesFromCoreWithoutCentralEl)

      if (rows.head.size - coreSize - 1 == begin) {
        output :+ updatedElement
      } else calculateHelp(begin + 1, output :+ updatedElement)
    }

    calculateHelp(0, Nil)
  }

}
