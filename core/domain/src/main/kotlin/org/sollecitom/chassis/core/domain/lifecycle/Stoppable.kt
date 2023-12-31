package org.sollecitom.chassis.core.domain.lifecycle

import kotlinx.coroutines.runBlocking

interface Stoppable {

    suspend fun stop()
}

fun Stoppable.stopBlocking() {

    runBlocking { stop() }
}