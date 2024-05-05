package exceptions

final case class EmptyIntervalException(private val message: String = "Интервал не должен быть пустым",
                                        private val cause: Throwable = None.orNull) extends RuntimeException
