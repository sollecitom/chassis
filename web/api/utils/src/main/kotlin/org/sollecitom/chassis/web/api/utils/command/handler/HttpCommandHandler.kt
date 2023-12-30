package org.sollecitom.chassis.web.api.utils.command.handler

import org.http4k.core.Request
import org.http4k.core.Response
import org.sollecitom.chassis.correlation.core.domain.access.Access
import org.sollecitom.chassis.ddd.application.ApplicationCommand
import org.sollecitom.chassis.ddd.domain.Happening

interface HttpCommandHandler<out COMMAND : ApplicationCommand<RESULT, ACCESS>, RESULT, out ACCESS : Access> {

    val commandType: Happening.Type

    fun parseCommand(request: Request): COMMAND

    @Suppress("UNCHECKED_CAST")
    fun convertToResponse(result: Any?): Response {

        if (!isResultAcceptable(result)) error("Result $result is not processable by $this")
        return resultToResponse(result as RESULT)
    }

    fun isResultAcceptable(result: Any?): Boolean

    fun resultToResponse(result: RESULT): Response
}