package org.sollecitom.chassis.example.webapp.kweb.starter

import kotlinx.coroutines.coroutineScope
import kweb.Kweb
import kweb.h1
import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.configuration.utils.formatted
import org.sollecitom.chassis.configuration.utils.fromYamlResource
import org.sollecitom.chassis.logger.core.loggable.Loggable

suspend fun main() = coroutineScope<Unit> {

    val environment = rawConfiguration().also(::configureLogging)
    logger.info { environment.formatted() }

    Kweb(port = 9000) {
        doc.body {
            h1().text("Hello World!")
        }
    }
}

private object Starter : Loggable()

private val logger get() = Starter.logger

fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides Environment.fromYamlResource("default-configuration.yml")