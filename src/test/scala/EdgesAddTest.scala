
import org.junit.jupiter.api.Test
import service.EdgesService
import types.{Core, Input}

class EdgesAddTest{

  @Test
  def addEdgesToDataFrameSuccess(): Unit = {
    val core: Core = Core.apply(3).get
    val input: Input = Seq(1 to 3, 1 to 3, 1 to 3)
    val supplementedInput: Either[String, types.DataFrameWithAdditionalEdges] = EdgesService.addEdgesToDataFrame(input, core)
    assert(supplementedInput.isRight)
  }

}
