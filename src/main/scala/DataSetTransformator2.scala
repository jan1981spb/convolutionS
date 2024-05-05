import scala.annotation.tailrec
import scala.collection.immutable.AbstractSeq
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait DataSetTransformator2 extends ConvolutionService {

  def transform(input: Seq[Seq[Int]], coreSize: Int): Future[Seq[Seq[Double]]] = {
    val rowsGroups = input.sliding(coreSize, 1).toVector
    val rez = Future.traverse((0 until input.size - 2).toList)(index => calculateValuesFromRowsGroup(rowsGroups(index), coreSize))
    rez
  }

  private def calculateValuesFromRowsGroup(rows: Seq[Seq[Int]], coreSize: Int): Future[Seq[Double]] = {
    @tailrec
    def calculateHelp(begin: Int, output: Seq[Double]): Seq[Double]= {
      val parts = for {
        row <- rows
        rowPart <- Seq(row.slice(begin, coreSize + begin))
      } yield rowPart
      val valuesFromCore = parts.reduce(_:++_)
      val valuesFromCoreWithoutCentralEl = valuesFromCore.take(valuesFromCore.length / 2) :++ valuesFromCore.takeRight(valuesFromCore.length / 2)
      val updatedElement = foldElements(valuesFromCoreWithoutCentralEl)

      if (rows.head.size - coreSize - 1 == begin) {
        output :+ updatedElement
      } else calculateHelp(begin + 1, output :+ updatedElement)
    }

    Future(calculateHelp(0, Nil))
  }

}
