package org.sollecitom.chassis.web.api.utils.api

import org.http4k.core.Request
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.serialization.json.context.jsonSerde
import org.sollecitom.chassis.web.api.utils.headers.HttpHeaderNames

interface HttpApiDefinition {

    val headerNames: HttpHeaderNames
//    val openApiFileLocation: String
}

context(HttpApiDefinition)
fun Request.withInvocationContext(context: InvocationContext<*>): Request = header(headerNames.correlation.invocationContext, InvocationContext.jsonSerde.serialize(context).toString())