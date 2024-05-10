package service

import cats.Foldable
import types.{Core, Input, SupplementedDataFrame, SupplementedInput}

class EdgesService {

  def addEdgesToDataFrame(input: Input, core: Core): Either[String, SupplementedDataFrame] = {
    val additionalEdges = core.size / 2
    val edgeWithZeros = Vector.tabulate(additionalEdges)(_ * 0)
    val supplementedWithVerticalEdges: SupplementedInput = input.map(row => edgeWithZeros ++: row :++ edgeWithZeros)
    val newRowWithZeros = Vector.tabulate(supplementedWithVerticalEdges.head.size)(_ * 0)

    val supplementedWithHorizontalEdges = Foldable[Seq].foldLeft((1 to additionalEdges), supplementedWithVerticalEdges) {
      case (acc, _) => newRowWithZeros +: acc :+ newRowWithZeros
    }
    SupplementedDataFrame(supplementedWithHorizontalEdges)
  }

}
object EdgesService extends EdgesService
