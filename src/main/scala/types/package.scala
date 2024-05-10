package object types {

  type Input = Seq[Seq[Int]]
  type SupplementedInput = Input
  type Batch = Input
  type BatchesContent = Seq[((Int, Int), Batch)]
  type Output = Input


  final case class InputDataFrame(input: Input)

  object InputDataFrame {
    def apply(input: Input): Either[String, InputDataFrame] = input match {
      case in if (in.size == in.head.size) => Right(new InputDataFrame(in))
      case _ => Left("width and height of input dataframe are not equal")
    }
  }

  final case class SupplementedDataFrame(input: Input)

  object SupplementedDataFrame {
    def apply(input: Input): Either[String, SupplementedDataFrame] = input match {
      case in if (in.size == in.head.size) => Right(new SupplementedDataFrame(in))
      case _ => Left("width and height of supplemented dataframe are not equal")
    }
  }

  trait BatchesFrame

  final case class SquareMatrixBatchesFrameFrame(batches: BatchesContent) extends BatchesFrame

  object SquareMatrixBatchesFrameFrame {
    def apply(batches: BatchesContent): Either[String, SquareMatrixBatchesFrameFrame] = batches match {
      case batches if ((batches.nonEmpty && Math.sqrt(batches.size).isValidInt)) => Right(new SquareMatrixBatchesFrameFrame(batches))
      case batches => Left(s"Quantity of batches must be represent as Square Matrix, such as (2*2=4), (3*3=9), (4*4=16), ect. But ${batches.size} is actual")
    }
  }

  final case class Core(size: Int)

  object Core {
    def apply(size: Int): Option[Core] = size match {
      case size if size % 2 != 0 => Some(new Core(size))
      case _ => None
    }
  }

}
