package com.element.dpg.libs.chassis.web.api.utils.api

import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.correlation.core.serialization.json.context.jsonSerde
import org.http4k.core.Request

interface HttpApiDefinition {

    val headerNames: com.element.dpg.libs.chassis.web.api.utils.headers.HttpHeaderNames
}

context(HttpApiDefinition)
fun Request.withInvocationContext(context: InvocationContext<*>): Request = header(headerNames.correlation.invocationContext, InvocationContext.jsonSerde.serialize(context).toString())