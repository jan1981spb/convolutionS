import types.{Core, OutputDataFrame}

object MainConv {

  def main(args: Array[String]): Unit = {

    new ConsecutiveConvolutionFacade().convolute(Core.apply(3).get)
  }

}
