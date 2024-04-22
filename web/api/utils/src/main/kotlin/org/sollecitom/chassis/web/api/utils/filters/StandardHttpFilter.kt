package org.sollecitom.chassis.web.api.utils.filters

import org.http4k.core.Filter
import org.http4k.core.NoOp
import org.http4k.core.then
import org.http4k.filter.*
import org.sollecitom.chassis.http4k.utils.lens.AddContentLength
import org.sollecitom.chassis.web.api.utils.api.HttpApiDefinition
import org.sollecitom.chassis.web.api.utils.filters.correlation.InvocationContextFilter
import org.sollecitom.chassis.web.api.utils.filters.correlation.addInvocationContextToLoggingStack
import org.sollecitom.chassis.web.api.utils.filters.correlation.parseInvocationContextFromGatewayHeader
import org.sollecitom.chassis.web.api.utils.filters.correlation.parseInvocationContextFromRequest

object StandardHttpFilter { // TODO add metrics

    context(HttpApiDefinition)
    fun forRequests(): Filter = ServerFilters.catchAndLogErrors.then(ServerFilters.CatchLensFailure).then(RequestFilters.GunZip()).then(DebuggingFilters.PrintRequestAndResponse().inIntelliJOnly()).then(ServerFilters.InitialiseRequestContext(RequestContextsProvider.requestContexts)).then(InvocationContextFilter.parseInvocationContextFromGatewayHeader()).then(InvocationContextFilter.addInvocationContextToLoggingStack())

    fun forResponses(): Filter = ResponseFilters.GZip().then(ResponseFilters.AddContentLength)
}

object GatewayHttpFilter { // TODO add metrics

    context(HttpApiDefinition)
    fun forRequests(): Filter = ServerFilters.catchAndLogErrors.then(ServerFilters.CatchLensFailure).then(DebuggingFilters.PrintRequestAndResponse().inIntelliJOnly()).then(InvocationContextFilter.parseInvocationContextFromRequest()).then(ServerFilters.InitialiseRequestContext(RequestContextsProvider.requestContexts)).then(InvocationContextFilter.parseInvocationContextFromGatewayHeader()).then(InvocationContextFilter.addInvocationContextToLoggingStack())

    fun forResponses(): Filter = Filter.NoOp
}