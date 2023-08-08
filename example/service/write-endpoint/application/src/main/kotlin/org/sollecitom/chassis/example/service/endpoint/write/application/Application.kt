package org.sollecitom.chassis.example.service.endpoint.write.application

interface Application {

    suspend operator fun <RESULT> invoke(command: ApplicationCommand<RESULT>): RESULT

    companion object
}