class DataSetService {

  def generateDataSetWithIncreaseNumbersInEachRow(batchesNumber: Int, batchSize: Int) = {
    for  {
      _ <- 0 to batchesNumber
      batch = (1 to 9).toList
    } yield batch
  }

  def addEdgesToDataSet(input: Seq[Seq[Int]], additionalEdges: Int): Seq[Seq[Int]] = {
    val edgeWithZeros = Vector.tabulate(additionalEdges)(_ * 0)
    val updatedWithVerticalEdges: Seq[Seq[Int]] = input.map(row => edgeWithZeros ++: row :++ edgeWithZeros)

    val nestedArrayWithZeros = Vector.tabulate(updatedWithVerticalEdges.head.size)(_ * 0)
    val updatedWithHorizontalEdges = (1 to additionalEdges)
      .foldLeft(updatedWithVerticalEdges) {
        (acc, _) => nestedArrayWithZeros +: acc :+ nestedArrayWithZeros
      }
    updatedWithHorizontalEdges
  }

}

object DataSetService extends DataSetService
