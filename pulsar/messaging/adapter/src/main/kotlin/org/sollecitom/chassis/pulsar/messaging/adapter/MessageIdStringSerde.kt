package org.sollecitom.chassis.pulsar.messaging.adapter

import org.apache.pulsar.client.api.MessageId
import org.apache.pulsar.client.api.MessageIdAdv
import org.sollecitom.chassis.messaging.domain.Message
import org.sollecitom.chassis.messaging.domain.Topic

@OptIn(ExperimentalStdlibApi::class)
internal object MessageIdStringSerde { // TODO revisit

    private const val TOPIC_PREFIX = "-:TOPIC:"
    private const val ID_PREFIX = "-:ID:"

    fun serialize(id: Message.Id): String = "$TOPIC_PREFIX${id.topic.fullName.value}$ID_PREFIX${(id as PulsarMessageId).toByteArray().serializeToString()}"

    fun deserialize(value: String): Message.Id {

        val idHexString = value.substringAfterLast(ID_PREFIX)
        val topicFullName = value.replace(TOPIC_PREFIX, "").substringBeforeLast(ID_PREFIX)
        val topic = Topic.parse(topicFullName)
        val id = MessageId.fromByteArrayWithTopic(idHexString.deserializeToByteArray(), topicFullName)
        return (id as MessageIdAdv).adapted(topic)
    }

    private fun ByteArray.serializeToString(): String {

        return toHexString()
    }

    private fun String.deserializeToByteArray(): ByteArray {

        return hexToByteArray()
    }
}