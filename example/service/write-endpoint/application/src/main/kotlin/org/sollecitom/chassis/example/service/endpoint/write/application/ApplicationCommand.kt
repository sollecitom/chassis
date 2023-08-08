package org.sollecitom.chassis.example.service.endpoint.write.application

import org.sollecitom.chassis.ddd.domain.Command

sealed interface ApplicationCommand<out RESULT> : Command {

    companion object
}