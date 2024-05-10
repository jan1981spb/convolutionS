package service

import cats.Foldable
import types.{Batch, SquareMatrixBatchesFrameFrame, SupplementedInput}

object BatchService {

  def createBatches[Batches](input: SupplementedInput, batchesQuantity: Int)(implicit b: BatchesFactory[Batches]): Either[String, Batches] = b.createButches(input, batchesQuantity)

  trait BatchesFactory[Batches] {
    def createButches(input: SupplementedInput, batchesQuantity: Int): Either[String, Batches]
  }

  object BatchesFactory {

    implicit object SquareMatrixBatchesFactory extends BatchesFactory[SquareMatrixBatchesFrameFrame] {
      override def createButches(input: SupplementedInput, batchesQuantity: Int): Either[String, SquareMatrixBatchesFrameFrame] = {
        val inputWightSize = input.head.size
        val batchesPerRow = Math.sqrt(batchesQuantity)
        val batchWight = (inputWightSize / batchesPerRow).toInt

        val intervalsWithIndexes = ((0 to (batchWight) by batchesPerRow.toInt))
          .map { i => (i, i + batchWight) }
          .zipWithIndex.map(pair => ((pair._2), (pair._1))).toMap

        val batches: Seq[((Int, Int), Batch)] = (for {
          indexInterval4Row <- intervalsWithIndexes.keys
          batch <- retrieveBatchesFromRow(input, indexInterval4Row, intervalsWithIndexes)
        } yield (batch)).toList sortBy (pair => pair._1._2)
        SquareMatrixBatchesFrameFrame(batches)
      }

      def retrieveBatchesFromRow(input: SupplementedInput, indexInterval4Row: Int, intervalsWithIndexes: Map[Int, (Int, Int)]): Seq[((Int, Int), Batch)] = {
        intervalsWithIndexes.get(indexInterval4Row) match {
          case None => Seq((0, 0) -> Seq.empty)
          case Some(interval) => {
            val rowsGroup = input.slice(interval._1, interval._2 + 1).toList

            Foldable[List].foldLeft(intervalsWithIndexes.keys.toList, Seq.empty[((Int, Int), Batch)]) {
              (outputBatches, columnsBatchIndex) => {

                val batch = Foldable[List].foldLeft(rowsGroup, Seq.empty[Seq[Int]]) {
                  (batch: Seq[Seq[Int]], row: Seq[Int]) => {
                    val interval = intervalsWithIndexes.getOrElse(columnsBatchIndex, (-1, -1))
                    val valuesPart4SelectedRow = row.slice(interval._1, interval._2 + 1)
                    (batch :+ valuesPart4SelectedRow)
                  }
                }

                val batchIndex = (indexInterval4Row, columnsBatchIndex)
                outputBatches :+ ((batchIndex, batch))
              }
            }

          }
        }
      }

    }

  }

}
