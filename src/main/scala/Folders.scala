class MeanConvolutionTransformatorService extends DataSetTransformator2 with MeanConvolutionService

trait MeanConvolutionService extends ConvolutionService {
  override def foldElements(valuesFromElementNeighborhood: Seq[Int]): Double = valuesFromElementNeighborhood.sum / valuesFromElementNeighborhood.size
}
