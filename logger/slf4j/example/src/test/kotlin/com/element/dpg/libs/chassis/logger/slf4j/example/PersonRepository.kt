package com.element.dpg.libs.chassis.logger.slf4j.example

interface PersonRepository {

    fun findById(id: String): Person?
}
