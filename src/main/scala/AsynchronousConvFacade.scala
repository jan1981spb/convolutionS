import BatchMaker.{retrieveButchesFromRow, validateBatchesNumber}
import exceptions.BatchesNumberException
import types.{Batches, Output, OutputPartition}

import java.lang.Thread.sleep
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.language.{implicitConversions, postfixOps}
import scala.util.{Failure, Success}

class AsynchronousConvFacade(conv: MeanConvolutionTransformatorService) {

 // val conv = new MeanConvolutionTransformatorService

  def convolute(input: Seq[Seq[Int]], batchesNumber: Int): Unit = {
    validateBatchesNumber(batchesNumber)
    val inputWightSize = input.head.size
    val batchWightSize = (inputWightSize / Math.sqrt(batchesNumber)).toInt
    val intervalsWithIndexes = ((0 until batchesNumber) by batchWightSize)
      .map { i => (i, i + batchWightSize - 1) }
      .zipWithIndex.map(pair => ((pair._2), (pair._1))).toMap

    val outputWait: Future[Iterable[Seq[Seq[Double]]]] = for {
      batches: Batches <- Future.traverse(intervalsWithIndexes.keys)(index => Future(retrieveButchesFromRow(input, index, intervalsWithIndexes)))
      .map(list => list.head)
      outputPart <- Future.traverse(batches.keys)(indexKey => conv.transform(batches.getOrElse(indexKey, Seq(Seq(0,0))), coreSize = 3))
    } yield outputPart

    val flattenOutput = outputWait.map(list => list.foldLeft(Seq.empty[Seq[Double]])((acc, el)=> acc :++ el))

    Await.ready(flattenOutput, 5 seconds)

    flattenOutput onComplete  {
      case Success(out) => println("Успешный результат вычислений " + out)
      case Failure(out) => println("Ошибка вычислений " + out)
    }
  }

  // TODO Дублируется
  private def validateBatchesNumber(batchesNumber: Int): Unit = {
    if (batchesNumber == 0 || !Math.sqrt(batchesNumber).isValidInt) {
      throw BatchesNumberException()
    }
  }

}
