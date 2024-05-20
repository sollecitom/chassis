package com.element.dpg.libs.chassis.web.api.utils.handler

import org.http4k.core.Request
import org.http4k.core.Response
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.Happening

interface HttpCommandHandler<out COMMAND : Command<RESULT, *>, RESULT> {

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