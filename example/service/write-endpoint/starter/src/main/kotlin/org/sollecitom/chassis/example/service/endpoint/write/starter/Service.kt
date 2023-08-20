package org.sollecitom.chassis.example.service.endpoint.write.starter

import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.configuration.utils.fromYamlResource
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.WebAPI
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.from
import org.sollecitom.chassis.example.service.endpoint.write.application.Application
import org.sollecitom.chassis.example.service.endpoint.write.application.ApplicationCommand
import org.sollecitom.chassis.example.service.endpoint.write.application.invoke
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.example.service.endpoint.write.configuration.ApplicationProperties
import org.sollecitom.chassis.example.service.endpoint.write.domain.user.User
import org.sollecitom.chassis.logger.core.loggable.Loggable
import org.sollecitom.chassis.web.service.domain.WebService

// TODO add WithCoreGenerators to application, etc.
class Service(private val environment: Environment) : WebService {

    private val application: Application = application()
    private val webAPI = webApi(application, environment)

    override val port: Int get() = webAPI.servicePort
    override val healthPort: Int get() = webAPI.healthPort
    override val host = "localhost" // TODO fix

    override suspend fun start() {
        webAPI.start()
        logger.info { ApplicationProperties.SERVICE_STARTED_LOG_MESSAGE }
    }

    override suspend fun stop() {
        webAPI.stop()
        logger.info { "Stopped" }
    }

    private fun application(): Application = object : Application {

        override suspend fun <RESULT> invoke(command: ApplicationCommand<RESULT>): RESULT {
            return RegisterUser.V1.Result.Accepted as RESULT // TODO fix
        }
    }
    private fun webApi(application: Application, environment: Environment) = WebAPI(configuration = WebAPI.Configuration.from(environment), application = application)

    companion object : Loggable()
}

// TODO move?
// TODO to pass a file names to this, from command args (for config-map and secrets exposed as files)
fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides Environment.fromYamlResource("default-configuration.yml")
//fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.fromConfigFile(File("secrets.yml")) overrides Environment.fromConfigFile(File("configuration.yml")) overrides Environment.ENV overrides Environment.fromResource("default-configuration.yml")