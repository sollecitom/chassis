package com.element.dpg.libs.chassis.core.domain.lifecycle

import kotlinx.coroutines.runBlocking

interface Startable {

    suspend fun start()
}

fun Startable.startBlocking() {

    runBlocking { start() }
}