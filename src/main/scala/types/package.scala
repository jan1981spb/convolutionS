package object types {

  type Batches = Map[(Int, Int), Seq[Seq[Int]]]
  type OutputPartition = Seq[Seq[Double]]
  type Output = OutputPartition

}
