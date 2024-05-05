package sandbox

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

object FuturesMain extends App {

  def add(value: Int) = Future.successful{
    Thread.sleep(500)
    println(value + 1)
    value + 1
  }

  val f = for {
    v1 <- add(1)
    v2 <- Future.failed(new RuntimeException("v1"))
    v3 <- add(v2)
  } yield List(v1, v2, v3)

 // val f = Future.traverse((1 to 3).toList)(index=>Future{println(index)})

  Await.ready(f, Duration.Inf)

  val recovered = f.recoverWith{
    case r: RuntimeException => Future(10)
   // case Failure(_) => Success(10)
  }

  println(recovered)
}
