package service

import cats.Foldable
import types.{Batch, SquareMatrixBatchesFrameFrame, InputWithAdditionalEdges}

object BatchService {

  def createBatches[BatchesFrame](input: InputWithAdditionalEdges, batchesQuantity: Int)(implicit b: BatchesFactory[BatchesFrame]): Either[String, BatchesFrame] = b.createButches(input, batchesQuantity)

  trait BatchesFactory[BatchesFrame] {
    def createButches(input: InputWithAdditionalEdges, batchesQuantity: Int): Either[String, BatchesFrame]
  }

  object BatchesFactory {

    /**
    Реализация нарезания множества на фрагменты (batches), в результаты которой получается квадратная матрица фрагментов.
    Так следующее множество разрезается на 4 фрагмента (2 в первой стоке матрицы, 2 - во второй).
                0 0 0 0 0 0      [  0 0 0 0          0 0 0 0        0 1 2 3         2 3 4 0       ]
                0 1 2 3 4 0      [  0 1 2 3          2 3 4 0        0 1 2 3         2 3 4 0       ]      fr11  fr12
                0 1 2 3 4 0   => [  0 1 2 3  fr11,   2 3 4 0 fr12 , 0 1 2 3  rf21,  2 3 4 0  fr22 ] =>   fr21  fr22
                0 1 2 3 4 0      [  0 1 2 3          2 3 4 0        0 0 0 0         0 0 0 0       ]
                0 1 2 3 4 0
                0 0 0 0 0 0`
     */
    implicit object SquareMatrixBatchesFactory extends BatchesFactory[SquareMatrixBatchesFrameFrame] {
      override def createButches(input: InputWithAdditionalEdges, batchesQuantity: Int): Either[String, SquareMatrixBatchesFrameFrame] = {
        val inputWightSize = input.head.size
        // Для batchesQuantity = 4 и строки 0 1 2 3 4 0будут следующие batches в разрезе строки: [0 1 2 3], [2 3 4 0]. Всего 2.
        val batchesPerRow = Math.ceil(Math.sqrt(batchesQuantity))
        val batchWight = (inputWightSize / batchesPerRow).toInt

        // Интервалы для определения границ batches пересекаются.
        val intervalsWithIndexes = ((0 to (batchWight) by batchesPerRow.toInt))
          .map { i => (i, i + batchWight) }
          .zipWithIndex.map(pair => ((pair._2), (pair._1))).toMap

        val batches: Seq[((Int, Int), Batch)] = (for {
          indexInterval4Row <- intervalsWithIndexes.keys
          batch <- retrieveBatchesFromRowGroup(input, indexInterval4Row, intervalsWithIndexes)
        } yield (batch)).toList sortBy (pair => pair._1._2)
        SquareMatrixBatchesFrameFrame(batches)
      }

      def retrieveBatchesFromRowGroup(input: InputWithAdditionalEdges, indexInterval4Row: Int, intervalsWithIndexes: Map[Int, (Int, Int)]): Seq[((Int, Int), Batch)] =
        intervalsWithIndexes.get(indexInterval4Row) match {
          case None => Seq((0, 0) -> Seq.empty)
          case Some(interval) => {
            val rowsGroup = input.slice(interval._1, interval._2 + 1).toList
            Foldable[List].foldLeft(intervalsWithIndexes.keys.toList, Seq.empty[((Int, Int), Batch)]) {
              (outputBatches, columnsBatchIndex) => {
                val batch = retrieveOneBatchFromRowGroup(rowsGroup, columnsBatchIndex, intervalsWithIndexes)
                val batchIndex = (indexInterval4Row, columnsBatchIndex)
                outputBatches :+ ((batchIndex, batch))
              }
            }
          }
        }


      def retrieveOneBatchFromRowGroup(rowsGroup: Seq[Seq[Int]], columnsBatchIndex: Int, intervalsWithIndexes: Map[Int, (Int, Int)]): Batch =
        Foldable[Seq].foldLeft(rowsGroup, Seq.empty[Seq[Int]]) {
          (batch: Seq[Seq[Int]], row: Seq[Int]) => {
            val interval = intervalsWithIndexes.getOrElse(columnsBatchIndex, (-1, -1))
            val valuesPart4SelectedRow = row.slice(interval._1, interval._2 + 1)
            (batch :+ valuesPart4SelectedRow)
          }
        }
    }

  }

}
