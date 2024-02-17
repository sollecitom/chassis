package org.sollecitom.chassis.core.example.inmemory.eda.domain

import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.messaging.domain.Message
import org.sollecitom.chassis.messaging.domain.ReceivedMessage

data class InboundMessage<VALUE>(override val id: Message.Id, override val key: String, override val value: VALUE, override val properties: Map<String, String>, override val publishedAt: Instant, override val producerName: Name, override val context: Message.Context, private val acknowledge: suspend (InboundMessage<VALUE>) -> Unit) : ReceivedMessage<VALUE> {

    override suspend fun acknowledge() = acknowledge(this)
}