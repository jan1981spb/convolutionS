import exceptions.EvenCoreException

trait DataSetTransformator extends ConvolutionService {
  def transform(input: Seq[Seq[Int]], coreSize: Int): Seq[Seq[Double]] = {
    if (input.isEmpty) return Nil
    assert(input.size == input.head.size, "Высота и ширина входного массива не совпадают")
    val numberOfAdditionalEdges = calculateCountOfAdditionalEdges(coreSize)
    val inputUpdatedWithEdges = DataSetService.addEdgesToDataSet(input, numberOfAdditionalEdges)

    val outputWithLastRowIndex = input.foldLeft((Seq(Seq.empty[Double]), numberOfAdditionalEdges)) {
      (outputWithRowIndex, inputRow) => {
        val rowIndexRelative = outputWithRowIndex._2
        val updatedRow: Seq[Double] = inputRow.foldLeft((Vector.empty[Double], numberOfAdditionalEdges)) {
          (rowWithColumnIndex, _) => {
            val columnIndexRelative = rowWithColumnIndex._2
            val valuesFromElementNeighborhood = getValuesFromElementNeighborhood(inputUpdatedWithEdges, rowIndexRelative, columnIndexRelative, numberOfAdditionalEdges)
            val updatedElement = foldElements(valuesFromElementNeighborhood)
            (rowWithColumnIndex._1 :+ updatedElement, columnIndexRelative + 1)
          }
        }._1
        (outputWithRowIndex._1 :+ updatedRow, outputWithRowIndex._2 + 1)
      }

    }
    outputWithLastRowIndex._1.filterNot(seq => seq.isEmpty)
  }

  private def getValuesFromElementNeighborhood(inputUpdatedWithEdges: Seq[Seq[Int]], currentRowIndex: Int, columnIndex: Int, numberOfAdditionalEdges: Int): Seq[Int] = {
    val startRowsRange = currentRowIndex - numberOfAdditionalEdges
    val endRowsRange = currentRowIndex + numberOfAdditionalEdges
    val indexesOfAdditionalEdge = (0 until numberOfAdditionalEdges).toSet
    val nestedArrayWithZeros = Vector.tabulate(numberOfAdditionalEdges * 2 + 1)(_ * 0)

    val values: Seq[Seq[Int]] = for (index <- startRowsRange to endRowsRange) yield {
      if (indexesOfAdditionalEdge.contains(index)) {
        nestedArrayWithZeros
      } else {
        getCoreValuesFromRow(inputUpdatedWithEdges.apply(index), currentRowIndex, index, columnIndex, numberOfAdditionalEdges)
      }
    }
    values.flatten
  }

  private def getCoreValuesFromRow(row: Seq[Int], currentRowIndex: Int, iteratedRowIndex: Int, columnIndex: Int, numberOfAdditionalEdges: Int): Seq[Int] = {
    def isNotCurrentElement(index: Int) = !(iteratedRowIndex == currentRowIndex && index == columnIndex)

    val startRowsRange = columnIndex - numberOfAdditionalEdges
    val endRowsRange = columnIndex + numberOfAdditionalEdges
    for (index <- startRowsRange to endRowsRange if isNotCurrentElement(index)) yield row.apply(index)
  }

  private def calculateCountOfAdditionalEdges(coreSize: Int): Int = {
    if (coreSize % 2 == 0) throw EvenCoreException()
    (coreSize - 1) / 2
  }

}


