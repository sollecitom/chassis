package com.element.dpg.libs.chassis.messaging.domain

data class OutboundMessage<out VALUE>(override val key: String, override val value: VALUE, override val properties: Map<String, String>, override val context: Message.Context) : Message<VALUE>