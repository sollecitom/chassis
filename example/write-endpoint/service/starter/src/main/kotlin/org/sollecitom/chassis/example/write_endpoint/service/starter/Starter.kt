package org.sollecitom.chassis.example.write_endpoint.service.starter

import kotlinx.coroutines.coroutineScope
import org.sollecitom.chassis.configuration.utils.StandardEnvironment
import org.sollecitom.chassis.example.write_endpoint.configuration.configureLogging
import java.io.File

suspend fun main(vararg args: String): Unit = coroutineScope {

    val environment = StandardEnvironment(defaultConfiguration = StandardEnvironment.defaultYamlConfiguration, additionalExternalConfigFiles = args.toList().additionalExternalConfigFiles())
    configureLogging(environment)
    Service(environment).start()
}

private fun List<String>.additionalExternalConfigFiles(): List<File> {

    // TODO read args for the presence of external additional config files
    return emptyList()
}
