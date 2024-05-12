import service.{BatchService, InmemoryDataFrameSource, EdgesService, MeanConvolutionTransformationService}
import types.{Core, SquareMatrixBatchesFrameFrame}

class ConsecutiveConvolutionFacade {

  def convolute(core: Core): Unit = {
    val conv = new MeanConvolutionTransformationService

    val result= for {
      inputFrame <- InmemoryDataFrameSource.generateDataFrameWithIncreaseNumbersInEachRow(4, 4)
      dataFrameWithAddEdges <- EdgesService.addEdgesToDataFrame(inputFrame.input, core)
      batchesFrame <- BatchService.createBatches[SquareMatrixBatchesFrameFrame](dataFrameWithAddEdges.input, 4)
      output <- conv.transform(batchesFrame.batches, core.size, inputFrame.input.flatten.size)
    } yield output

    result match {
      case Right(output) => print(s"Успешно проведена свертка - результат : $output")
      case Left(er) => print(s"Не удалось реализовать свертку по причине : $er")
    }
  }
}
