package service

import cats.Foldable
import types.{BatchesContent, OutputDataFrame}

import scala.annotation.tailrec

class MeanConvolutionTransformationService extends TransformationService with MeanConvolutionService

trait ConvolutionService {
  def foldElements(valuesFromElementNeighborhood: Seq[Int]): Double
}

trait MeanConvolutionService extends ConvolutionService {
  override def foldElements(valuesFromElementNeighborhood: Seq[Int]): Double = valuesFromElementNeighborhood.sum / valuesFromElementNeighborhood.size
}

trait TransformationService extends ConvolutionService {

  def transform(batches: BatchesContent, coreSize: Int, inputSize: Int): Either[String, OutputDataFrame] = {
    val output = Foldable[Seq].foldLeft(batches, Seq.empty[((Int, Int), (Seq[Double]))]) {
      case (output, batchWithIndex) =>
          val rowsGroups = batchWithIndex._2.sliding(coreSize, 1).toVector
          val outputSubSetWithIndex = Foldable[Seq].foldLeft(rowsGroups, ((0, 0), Seq.empty[Double])) {
            case (output, group) => (batchWithIndex._1, output._2 :++ calculateValuesFromRowsGroup(group, coreSize))
          }
      output :+ outputSubSetWithIndex
    }
    OutputDataFrame(output, inputSize)
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

      if (rows.head.size - coreSize == begin) {
        output :+ updatedElement
      } else calculateHelp(begin + 1, output :+ updatedElement)
    }

    calculateHelp(0, Nil)
  }

}
