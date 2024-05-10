import types.{Core, OutputDataFrame}

object MainConv {

  def main(args: Array[String]): Unit = {

    val intervalsWithIndexes = ((0 to 3) by 2)
      .map { i => (i, i + 3) }
      .zipWithIndex.map(pair => ((pair._2), (pair._1))).toMap
    print(intervalsWithIndexes.slice(-1, -1))

  }

}
