package object types {

  type Batches = Map[(Int, Int), Seq[Seq[Int]]]
  type Input = Seq[Seq[Int]]
  type SupplementedInput = Input
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

  final case class Core(size: Int)
  object Core {
    def apply(size: Int): Option[Core] = size match {
      case size if size % 2 != 0 => Some(new Core(size))
      case _ => None
    }
  }

}
