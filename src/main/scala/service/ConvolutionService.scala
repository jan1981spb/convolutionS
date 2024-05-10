package service

trait ConvolutionService {
  def foldElements(valuesFromElementNeighborhood: Seq[Int]): Double
}
