package org.sollecitom.chassis.example.service.endpoint.write.starter

import kotlinx.coroutines.test.runTest
import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.configuration.utils.from
import org.sollecitom.chassis.lens.core.extensions.networking.healthPort
import org.sollecitom.chassis.lens.core.extensions.networking.servicePort
import org.sollecitom.chassis.logging.standard.configuration.StandardLoggingConfiguration

@TestInstance(PER_CLASS)
private class ServiceTest {

    @Test
    fun `system test`() = runTest {

        val environment = Environment.from(EnvironmentKey.servicePort to "0", EnvironmentKey.healthPort to "0")
        StandardLoggingConfiguration.invoke(environment)

        val service = Service(environment).start()

        service.stop()
    }
}