package org.sollecitom.chassis.web.api.utils.filters

import org.http4k.core.ContentType
import org.http4k.core.Filter
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.filter.ServerFilters
import org.sollecitom.chassis.http4k.utils.lens.contentType
import org.sollecitom.chassis.logger.core.loggable.Loggable

private object CatchAndLogErrorsRequestFilter : Loggable() {

    operator fun invoke(): Filter = Filter { next ->
        {
            try {
                next(it)
            } catch (error: Throwable) {
                if (error !is Exception) throw error
                logger.error(error) { "An error was caught while processing request ${it.toMessage()}" }
                Response(Status.INTERNAL_SERVER_ERROR).contentType(ContentType.TEXT_PLAIN).body("Something went wrong on the server.") // TODO switch it to JSON?
            }
        }
    }
}

val ServerFilters.catchAndLogErrors: Filter get() = CatchAndLogErrorsRequestFilter()