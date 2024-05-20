package com.element.dpg.libs.chassis.web.api.utils

import com.element.dpg.libs.chassis.correlation.core.domain.context.InvocationContext
import com.element.dpg.libs.chassis.correlation.core.serialization.json.context.jsonSerde
import org.http4k.core.Request

fun Request.withInvocationContext(headerName: String, context: InvocationContext<*>): Request = header(headerName, InvocationContext.jsonSerde.serialize(context).toString())