package com.element.dpg.libs.chassis.web.api.utils.handler

import com.element.dpg.libs.chassis.ddd.domain.Command
import com.element.dpg.libs.chassis.ddd.domain.Happening
import org.http4k.core.Request
import org.http4k.core.Response

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