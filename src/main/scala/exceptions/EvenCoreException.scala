package exceptions

final case class EvenCoreException(private val message: String = "Размер ядра должен быть нечетным",
                                   private val cause: Throwable = None.orNull) extends RuntimeException
