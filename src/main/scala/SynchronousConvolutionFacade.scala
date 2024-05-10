import service.{DataFrameSource, EdgesService}
import types.{Batches, Core, InputDataFrame, Output}

class SynchronousConvolutionFacade {

  def convolute(core: Core):Output = {

    for {
      inputFrame <- DataFrameSource.generateDataFrameWithIncreaseNumbersInEachRow(9, 9)
      dataFrameWithAddEdges <- EdgesService.addEdgesToDataFrame(inputFrame.input, core)

    } yield Right(dataFrameWithAddEdges)

    val batches: Batches = ???
    val output: Output = ???
    output
  }
}
