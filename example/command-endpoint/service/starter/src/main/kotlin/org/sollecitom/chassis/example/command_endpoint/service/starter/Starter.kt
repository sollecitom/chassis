package org.sollecitom.chassis.example.command_endpoint.service.starter

import kotlinx.coroutines.runBlocking
import org.sollecitom.chassis.configuration.utils.StandardEnvironment
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
import java.io.File

fun main(args: Array<String>) = runBlocking { // no suspend coroutineScope {} or it won't work with Docker

    val environment = StandardEnvironment(defaultConfiguration = StandardEnvironment.defaultYamlConfiguration, additionalExternalConfigFiles = additionalExternalConfigFiles(args))
    configureLogging(environment)
    Service(environment).start()
}

private fun additionalExternalConfigFiles(args: Array<String>): List<File> {

    // TODO read args for the presence of external additional config files
    return emptyList()
}
