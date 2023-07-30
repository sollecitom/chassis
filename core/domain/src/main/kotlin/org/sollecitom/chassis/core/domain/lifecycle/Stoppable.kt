package org.sollecitom.chassis.core.domain.lifecycle

interface Stoppable {

    suspend fun stop()
}