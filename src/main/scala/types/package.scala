package object types {

  type Input = Seq[Seq[Int]]
  type InputWithAdditionalEdges = Input
  type Batch = Input
  type BatchesContent = Seq[((Int, Int), Batch)]
  type OutputSubset = Seq[Double]
  type Output = Seq[((Int, Int), OutputSubset)]

  final case class InputDataFrame(input: Input)
  object InputDataFrame {
    def apply(input: Input): Either[String, InputDataFrame] = input match {
      case in if (in.size == in.head.size) => Right(new InputDataFrame(in))
      case _ => Left("Ширина и высота исходного множества не равны")
    }
  }

  final case class DataFrameWithAdditionalEdges(input: Input)
  object DataFrameWithAdditionalEdges {
    def apply(input: Input): Either[String, DataFrameWithAdditionalEdges] = input match {
      case in if (in.size == in.head.size) => Right(new DataFrameWithAdditionalEdges(in))
      case _ => Left("Ширина и высота множества, дополненного гранями, не равны")
    }
  }

  trait BatchesFrame

  final case class SquareMatrixBatchesFrameFrame(batches: BatchesContent) extends BatchesFrame
  object SquareMatrixBatchesFrameFrame {
    def apply(batches: BatchesContent): Either[String, SquareMatrixBatchesFrameFrame] = batches match {
      case batches if ((batches.nonEmpty && Math.sqrt(batches.size).isValidInt)) => Right(new SquareMatrixBatchesFrameFrame(batches))
      case batches => Left(s"Количество butches должно соответствовать размеру квадратной матрицы, к примеру (2*2=4), (3*3=9), (4*4=16), и тд. По факту: ${batches.size}")
    }
  }

  final case class Core(size: Int)
  object Core {
    def apply(size: Int): Option[Core] = size match {
      case size if size % 2 != 0 => Some(new Core(size))
      case _ => None
    }
  }

  final case class OutputDataFrame(output: Output)
  object OutputDataFrame {
    def apply(output: Output, inputSize: Int): Either[String, OutputDataFrame] = output match {
      case output if isOutputSizeNotEqualInputSize(output, inputSize) => Left(s"Размер нового множества не соответствует размеру исходного: $inputSize ")
      case output => Right(new OutputDataFrame(output))
    }

    def isOutputSizeNotEqualInputSize(output: Output, inputSize: Int): Boolean = {
      val flattenOutput = output.flatMap(o => o._2)
      flattenOutput.size != inputSize
    }
  }

}
