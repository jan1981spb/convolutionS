package service

import types.InputDataFrame

class DataFrameSource {

  def generateDataFrameWithIncreaseNumbersInEachRow(inputHeigth: Int, inputWeight: Int): Either[String, InputDataFrame] = {
    val input = for  {
      _ <- 0 until inputHeigth
      batch = (1 to inputWeight).toList
    } yield batch
    InputDataFrame(input)
  }
}
object DataFrameSource extends DataFrameSource

