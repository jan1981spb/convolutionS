import org.junit.jupiter.api.Assertions.{assertEquals, assertNotNull, assertThrows}
import org.junit.jupiter.api.Test

class TransformationTest {

  @Test
  def transformWithMeanSuccess(): Unit = {
//    val conv = new MeanConvolutionTransformatorService
//    val input = Vector(Vector(1,2,3), Vector(1,2,3), Vector(1,2,3))
//    val output = conv.transform(input, coreSize = 3)
//    assertNotNull(output,"Новый массив не должен быть нулевым")
//    assertEquals(output, Seq(Vector(0.0, 1.0, 0.0), Vector(1.0, 2.0, 1.0), Vector(0.0, 1.0, 0.0)))
  }

  @Test
  def transformInput_WithNotSame_HeightAndWight_throwError(): Unit = {
//    val conv = new MeanConvolutionTransformatorService
//    val input = Vector(Vector(1,2,3), Vector(1,2,3))
  //  assertThrows[AssertionError](conv.transformWithConvolution(input, coreSize = 3))
  }
}
