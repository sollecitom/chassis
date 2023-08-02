package org.sollecitom.chassis.example.service.endpoint.write.starter

import kotlinx.coroutines.coroutineScope

suspend fun main(): Unit = coroutineScope {

    val environment = rawConfiguration()
    configureLogging(environment)
    Service(environment).start()
}