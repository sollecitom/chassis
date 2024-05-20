package com.element.dpg.libs.chassis.core.domain.lifecycle

import kotlinx.coroutines.runBlocking

interface Stoppable {

    suspend fun stop()
}

fun Stoppable.stopBlocking() {

    runBlocking { stop() }
}