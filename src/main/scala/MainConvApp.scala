import com.softwaremill.macwire.wire

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object MainConvApp extends App {

  val dataSetService = new DataSetService

  lazy val conv = wire[MeanConvolutionTransformatorService]

  val asynchronousConv = wire[AsynchronousConvFacade]

  val input = dataSetService.generateDataSetWithIncreaseNumbersInEachRow(batchesNumber = 9, batchSize = 9)

  asynchronousConv.convolute(input, 9)
  // Await.ready(rez, Duration.Inf)

//  asynchronousConv.convolute(input, 9)

 // val batchesWait = ButchMaker.createButches(input, 9)

//  val butches = Await.ready(butchesWait, Duration.Inf)
//
//  println("Received batches ")
//  butches.foreach(batch=>println(batch))

 // val outputWait: Future[Future[Iterable[Seq[Seq[Double]]]]] = batchesWait.map{
 //   batch => Future.traverse(batch.values)(value => Future(conv.transformWithConvolution(value, coreSize = 3)))
 // }

 // val output = Await.ready(outputWait, Duration.Inf)

 // println("Вывод" + output)

 // val output: Seq[Seq[Double]] = conv.transformWithConvolution(input, coreSize = 3)

//  println("Входные данные: ")
 // input.foreach(row => println(row))

//  println("Результат преобразования: ")
 // output.foreach(row => println(row))



}
