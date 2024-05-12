package service

import cats.Foldable
import types.{Core, Input, DataFrameWithAdditionalEdges, InputWithAdditionalEdges}

class EdgesService {

  /**
    Добавляет равное количество дополнительных строк и столбцов (заполненных нулями) с каждого края
    Так, для ядра размером любой стороны 3
        и исходного множества
                1 2 3 4
                1 2 3 4
                1 2 3 4   -
                1 2 3 4
        будет создано новое множество
                0 0 0 0 0 0
                0 1 2 3 4 0
                0 1 2 3 4 0
                0 1 2 3 4 0
                0 1 2 3 4 0
                0 0 0 0 0 0`
   */
  def addEdgesToDataFrame(input: Input, core: Core): Either[String, DataFrameWithAdditionalEdges] = {
    val additionalEdges = core.size / 2
    val edgeWithZeros = Vector.tabulate(additionalEdges)(_ * 0)
    val withNewVerticalEdges: InputWithAdditionalEdges = input.map(row => edgeWithZeros ++: row :++ edgeWithZeros)
    val newRowWithZeros = Vector.tabulate(withNewVerticalEdges.head.size)(_ * 0)

    val withNewHorizontalEdges = Foldable[Seq].foldLeft((1 to additionalEdges), withNewVerticalEdges) {
      case (acc, _) => newRowWithZeros +: acc :+ newRowWithZeros
    }
    DataFrameWithAdditionalEdges(withNewHorizontalEdges)
  }
}

object EdgesService extends EdgesService
