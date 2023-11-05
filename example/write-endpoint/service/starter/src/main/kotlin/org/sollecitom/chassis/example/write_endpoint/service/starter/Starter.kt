package org.sollecitom.chassis.example.write_endpoint.service.starter

import kotlinx.coroutines.runBlocking
import org.sollecitom.chassis.configuration.utils.StandardEnvironment
import org.sollecitom.chassis.example.write_endpoint.configuration.configureLogging
import java.io.File

fun main(args: Array<String>) = runBlocking { // no suspend coroutineScope {} or it won't work with Docker

    val environment = StandardEnvironment(defaultConfiguration = StandardEnvironment.defaultYamlConfiguration, additionalExternalConfigFiles = args.toList().additionalExternalConfigFiles())
    configureLogging(environment)
    Service(environment).start()
}

private fun List<String>.additionalExternalConfigFiles(): List<File> {

    // TODO read args for the presence of external additional config files
    return emptyList()
}
