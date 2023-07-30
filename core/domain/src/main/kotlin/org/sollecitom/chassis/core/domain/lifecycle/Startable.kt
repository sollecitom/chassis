package org.sollecitom.chassis.core.domain.lifecycle

import kotlinx.coroutines.runBlocking

interface Startable {

    suspend fun start()

    fun startBlocking() = runBlocking { start() }
}