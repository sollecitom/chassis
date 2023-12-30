package org.sollecitom.chassis.web.api.utils.command.handler

import org.http4k.core.Request
import org.http4k.format.AutoContentNegotiator
import org.http4k.lens.BiDiBodyLensSpec
import org.http4k.lens.ContentNegotiation
import org.sollecitom.chassis.ddd.application.ApplicationCommand
import org.sollecitom.chassis.ddd.domain.Happening
import org.sollecitom.chassis.web.api.utils.content.negotiation.auto
import kotlin.reflect.KClass

abstract class HttpCommandHandlerTemplate<COMMAND : ApplicationCommand<RESULT, *>, RESULT>(final override val commandType: Happening.Type, private val resultClass: KClass<*>, lensSpecs: List<BiDiBodyLensSpec<COMMAND>>) : HttpCommandHandler<COMMAND, RESULT> {

    private val lenses = lensSpecs.map(BiDiBodyLensSpec<COMMAND>::toLens)
    private val negotiator: AutoContentNegotiator<COMMAND> = ContentNegotiation.auto(lenses)
    private val command = negotiator.toBodyLens()

    final override fun parseCommand(request: Request) = command(request)

    final override fun isResultAcceptable(result: Any?) = resultClass.isInstance(result)
}