package org.sollecitom.chassis.example.service.endpoint.write.starter

import org.sollecitom.chassis.core.domain.lifecycle.Startable
import org.sollecitom.chassis.core.domain.lifecycle.Stoppable

// TODO move
interface WebService : Startable, Stoppable {

    val port: Int
    val healthPort: Int
}