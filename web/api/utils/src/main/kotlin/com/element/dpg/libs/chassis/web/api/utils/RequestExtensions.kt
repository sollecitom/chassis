package com.element.dpg.libs.chassis.web.api.utils

import org.http4k.core.Request
import org.sollecitom.chassis.correlation.core.domain.context.InvocationContext
import org.sollecitom.chassis.correlation.core.serialization.json.context.jsonSerde

fun Request.withInvocationContext(headerName: String, context: InvocationContext<*>): Request = header(headerName, InvocationContext.jsonSerde.serialize(context).toString())