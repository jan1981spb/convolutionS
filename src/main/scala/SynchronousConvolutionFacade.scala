import service.{BatchService, DataFrameSource, EdgesService, MeanConvolutionTransformationService}
import types.{Batch, BatchesContent, Core, SquareMatrixBatchesFrameFrame}

class SynchronousConvolutionFacade {

  def convolute(core: Core): Unit = {
    val conv = new MeanConvolutionTransformationService
    val result= for {
      inputFrame <- DataFrameSource.generateDataFrameWithIncreaseNumbersInEachRow(8, 8)
      dataFrameWithAddEdges <- EdgesService.addEdgesToDataFrame(inputFrame.input, core)
      batchesFrame <- BatchService.createBatches[SquareMatrixBatchesFrameFrame](dataFrameWithAddEdges.input, 3)
      batch <- Right(batchesFrame.batches.map(content => content._2).head)
      output <- conv.transform(batch, core.size)
    } yield output
    result match {
      case Right(output) => print(s"Success calculated : $output")
      case Left(er) => print(s"Fail calculation : $er")
    }
  }
}
