import org.junit.jupiter.api.Test
import service.InmemoryDataFrameSource

class DataFrameTest {

  @Test
  def generateDataFrameWithIncreaseNumbersInEachRowSuccess(): Unit = {
      val df = InmemoryDataFrameSource.generateDataFrameWithIncreaseNumbersInEachRow(9, 9)
      assert(df.isRight)
  }

  @Test
  def generateDataFrameWithIncreaseNumbersInEachRowFailed(): Unit = {
    val df = InmemoryDataFrameSource.generateDataFrameWithIncreaseNumbersInEachRow(9, 8)
    assert(df.isLeft)
  }
}
