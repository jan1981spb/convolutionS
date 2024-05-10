import service.{BatchService, DataFrameSource, EdgesService}
import types.{Core, Output, SquareMatrixBatchesFrameFrame}

class SynchronousConvolutionFacade {

  def convolute(core: Core):Output = {

    for {
      inputFrame <- DataFrameSource.generateDataFrameWithIncreaseNumbersInEachRow(9, 9)
      dataFrameWithAddEdges <- EdgesService.addEdgesToDataFrame(inputFrame.input, core)
      batches <- BatchService.createBatches[SquareMatrixBatchesFrameFrame](dataFrameWithAddEdges.input, 3)

    } yield Right(batches)

    val output: Output = ???
    output
  }
}
