package com.element.dpg.libs.chassis.nats.client

import com.element.dpg.libs.chassis.core.domain.networking.Port
import io.nats.client.Options
import io.nats.client.impl.Headers

fun Options.Builder.server(host: String, port: Int) = server("nats://$host:$port")

fun Options.Builder.server(host: String, port: Port) = server("nats://$host:${port.value}")

fun Headers.toMultiMap(): Map<String, List<String>> = entrySet().associate { it.key to it.value }