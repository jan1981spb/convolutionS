package exceptions

final case class BatchesNumberException(private val message: String = "Из количества батчей должен извлекаться корень из двух без отстатка",
                                        private val cause: Throwable = None.orNull) extends RuntimeException
