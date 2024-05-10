import org.junit.jupiter.api.Test
import service.BatchService
import types.{BatchesContent, SquareMatrixBatchesFrameFrame, SupplementedInput}

class BatchesFrameTest {

  @Test
  def generateSquartMatrixBatchesSuccess(): Unit = {
    val input: SupplementedInput = Seq(
      Seq(0, 0, 0, 0, 0, 0),
      Seq(0, 1, 2, 3, 4, 0),
      Seq(0, 1, 2, 3, 4, 0),
      Seq(0, 1, 2, 3, 4, 0),
      Seq(0, 1, 2, 3, 4, 0),
      Seq(0, 0, 0, 0, 0, 0)
    )

    val df = BatchService.createBatches[SquareMatrixBatchesFrameFrame](input, 4)
    assert(df.isRight)

    val expected: BatchesContent = (List(((0, 0), List(List(0, 0, 0, 0),
      List(0, 1, 2, 3),
      List(0, 1, 2, 3),
      List(0, 1, 2, 3))),
      ((1, 0), List(List(0, 1, 2, 3),
        List(0, 1, 2, 3),
        List(0, 1, 2, 3),
        List(0, 0, 0, 0))),
      ((0, 1), List(List(0, 0, 0, 0),
        List(2, 3, 4, 0),
        List(2, 3, 4, 0),
        List(2, 3, 4, 0))),
      ((1, 1), List(List(2, 3, 4, 0),
        List(2, 3, 4, 0),
        List(2, 3, 4, 0),
        List(0, 0, 0, 0)))))

    df match {
      case Right(value) => assert(value.batches == expected)
    }
  }


}
