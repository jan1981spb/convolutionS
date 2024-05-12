package service

import types.InputDataFrame

trait DataFrameSource {
  def generateDataFrameWithIncreaseNumbersInEachRow(inputHeigth: Int, inputWeight: Int): Either[String, InputDataFrame]
}

class InmemoryDataFrameSource extends DataFrameSource {

  def generateDataFrameWithIncreaseNumbersInEachRow(inputHeigth: Int, inputWeight: Int): Either[String, InputDataFrame] = {
    val input = for  {
      _ <- 0 until inputHeigth
      batch = (1 to inputWeight).toList
    } yield batch
   InputDataFrame(input)
  }
}

object InmemoryDataFrameSource extends InmemoryDataFrameSource

