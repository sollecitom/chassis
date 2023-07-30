package org.sollecitom.chassis.core.domain.lifecycle

interface Startable {

    suspend fun start()
}