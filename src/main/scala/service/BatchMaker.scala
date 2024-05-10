

import exceptions.{BatchesNumberException, EmptyIntervalException}

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object BatchMaker {

  def createButches(input: Seq[Seq[Int]], batchesNumber: Int): Future[Map[(Int, Int), Seq[Seq[Int]]]] = {
    validateBatchesNumber(batchesNumber)
    val inputWightSize = input.head.size
    val butchWightSize = (inputWightSize / Math.sqrt(batchesNumber)).toInt
    val intervalsWithIndexes = ((0 until batchesNumber) by butchWightSize)
      .map { i => (i, i + butchWightSize - 1) }
      .zipWithIndex.map(pair => ((pair._2), (pair._1))).toMap

    val batches = Future.traverse(intervalsWithIndexes.keys)(index => Future(retrieveButchesFromRow(input, index, intervalsWithIndexes)))
      .map(list => list.foldLeft(Map.empty[(Int, Int), Seq[Seq[Int]]])((acc, batch) => acc ++ batch))

    //    val batches = (for {
    //      indexInterval4Row <- intervalsWithIndexes.keys
    //      batch <- retrieveButchesFromRow(input, indexInterval4Row, intervalsWithIndexes)
    //    } yield (batch)).toList sortBy (pair => pair._1._2)

    println("Expected batches ")
    batches.foreach(butch => println(butch))
    batches
  }

  def retrieveButchesFromRow(input: Seq[Seq[Int]], indexInterval4Row: Int, intervalsWithIndexes: Map[Int, (Int, Int)]): Map[(Int, Int), Seq[Seq[Int]]] = {
    val interval = getValueFromMap(indexInterval4Row, intervalsWithIndexes)
    val rows = input.slice(interval._1, interval._2 + 1).toList

    @tailrec
    def fillTheButchOutput(rows: Seq[Seq[Int]], output: Map[(Int, Int), Seq[Seq[Int]]]): Map[(Int, Int), Seq[Seq[Int]]] = {
      rows match {
        case Nil => output
        case row :: tail =>
          val updatedOutput = output.++(retrievePartsOfButchesFromColumns(intervalsWithIndexes, row, indexInterval4Row, output))
          fillTheButchOutput(tail, updatedOutput)
      }
    }

    fillTheButchOutput(rows, output = Map.empty[(Int, Int), Seq[Seq[Int]]])
  }

  private def retrievePartsOfButchesFromColumns(intervalsWithIndexes: Map[Int, (Int, Int)], row: Seq[Int], rowsButchIndex: Int, output: Map[(Int, Int), Seq[Seq[Int]]]) = {
    intervalsWithIndexes.keys.foldLeft(output) {
      (output, columnsButchIndex) => {
        val interval = getValueFromMap(columnsButchIndex, intervalsWithIndexes)
        val columnsPart4SelectedRow = row.slice(interval._1, interval._2 + 1)
        val butchIndex = (rowsButchIndex, columnsButchIndex)

        output.get(butchIndex) match {
          case None => output + (butchIndex -> List(columnsPart4SelectedRow))
          case Some(value) => output + (butchIndex -> (value :+ columnsPart4SelectedRow))
        }
      }
    }

  }

  private def getValueFromMap(indexInterval4Row: Int, intervalsWithIndexes: Map[Int, (Int, Int)]): (Int, Int) = {
    intervalsWithIndexes.get(indexInterval4Row) match {
      case Some(value) => value
      case None => throw new EmptyIntervalException
    }
  }

  private def validateBatchesNumber(batchesNumber: Int): Unit = {
    if (batchesNumber == 0 || !Math.sqrt(batchesNumber).isValidInt) {
      throw BatchesNumberException()
    }
  }

}



