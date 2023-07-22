package org.sollecitom.chassis.logger.slf4j.example

interface PersonRepository {

    fun findById(id: String): Person?
}
