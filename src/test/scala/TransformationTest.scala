import org.junit.jupiter.api.Assertions.{assertEquals, assertNotNull, assertThrows}
import org.junit.jupiter.api.Test
import service.MeanConvolutionTransformationService

class TransformationTest {

  @Test
  def transformWithMeanSuccess(): Unit = {
    val conv = new MeanConvolutionTransformationService
    val input = List(((0,0), List(List(0, 0, 0, 0),
                                  List(0, 1, 2, 3),
                                  List(0, 1, 2, 3),
                                  List(0, 1, 2, 3))))
    val output = conv.transform(input, coreSize = 3, inputSize = 4)
    assert(output.isRight)

    output match {
      case Right(value) => assertEquals(value.output, List(((0,0), Seq(0.0, 1.0, 1.0, 2.0))))
    }


    val input2 =  List(((0,0), List(List(0, 0, 0, 0, 0),
                                    List(0, 1, 2, 3, 4),
                                    List(0, 1, 2, 3, 4),
                                    List(0, 1, 2, 3, 4),
                                    List(0, 1, 2, 3, 4))))
    val output2 = conv.transform(input2, coreSize = 3, inputSize = 9)
    assert(output2.isRight)

    output2 match {
      case Right(value) => assertEquals(value.output, List(((0,0), List(0.0, 1.0, 1.0, 1.0, 2.0, 3.0, 1.0, 2.0, 3.0))))
    }

  }

  @Test
  def input_TransformedToOutput_withDifferSize_Fails(): Unit = {
    val conv = new MeanConvolutionTransformationService
    val input =  List(((0,0), List(List(0, 0, 0, 0, 0),
                                   List(0, 1, 2, 3, 4),
                                   List(0, 1, 2, 3, 4),
                                   List(0, 1, 2, 3, 4),
                                    List(0, 1, 2, 3, 4))))
     val output = conv.transform(input, coreSize = 3, inputSize = 4)
    assert(output.isLeft)

  }
}
