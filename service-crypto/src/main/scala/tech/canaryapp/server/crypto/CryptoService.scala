package tech.canaryapp.server.crypto

import monix.eval.Task
import tech.canaryapp.server.crypto.config.CryptoServiceConfig
import tech.canaryapp.server.service.Service
import tech.canaryapp.server.service.model.ServiceName

/**
  * @author Tomas Mihalicka <tomas@mihalicka.com>
  */
object CryptoService extends Service[CryptoServiceConfig] {

  override protected val serviceName: ServiceName = ServiceName("crypto")

  override protected def start(serviceConfig: CryptoServiceConfig): Task[Unit] = {

  }
}
