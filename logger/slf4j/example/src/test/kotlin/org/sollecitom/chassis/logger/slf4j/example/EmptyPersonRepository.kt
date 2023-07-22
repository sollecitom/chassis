package org.sollecitom.chassis.logger.slf4j.example

import org.sollecitom.chassis.logger.core.Loggable

class EmptyPersonRepository : PersonRepository {

    override fun findById(id: String): Person? {

        logger.trace { "Entered method `findById` with arguments: {`id`: \"${id}\"}" }
        if (id.isEmpty()) {
            logger.error { "Received illegal empty ID" }
            return null
        }
        logger.info { "Found no person for ID: $id" }
        return null
    }

    companion object : Loggable()
}