package org.sollecitom.chassis.messaging.domain

interface Message<out VALUE> {

    val key: String
    val value: VALUE
    val bytes: ByteArray
    val properties: Map<String, String>
    val context: Context

    fun hasProperty(propertyKey: String): Boolean = properties.containsKey(propertyKey)
    fun getPropertyOrNull(propertyKey: String): String? = properties[propertyKey]

    // TODO wanna add here support for encryption context?

    open class Id(val partitionIndex: Int, val serial: Long) {

        fun receivedOnTopic(topic: Topic): ReceivedMessage.Id = ReceivedMessage.Id(partitionIndex, serial, topic)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Id) return false

            if (partitionIndex != other.partitionIndex) return false
            if (serial != other.serial) return false

            return true
        }

        override fun hashCode(): Int {
            var result = partitionIndex
            result = 31 * result + serial.hashCode()
            return result
        }

        override fun toString() = "Message.Id(partitionIndex=$partitionIndex, serial=$serial)"

        operator fun component1(): Int = partitionIndex
        operator fun component2(): Long = serial
    }

    data class Context(val parentMessageId: ReceivedMessage.Id? = null, val originatingMessageId: ReceivedMessage.Id? = null) {

        init {
            require(parentMessageId == null && originatingMessageId == null || parentMessageId != null && originatingMessageId != null) { "Parent and originating message IDs must both be either specified or omitted" }
        }

        fun fork(parentMessageId: ReceivedMessage.Id) = copy(parentMessageId = parentMessageId, originatingMessageId = originatingMessageId ?: parentMessageId)

        val isOriginating: Boolean get() = originatingMessageId == null
    }

    interface WithSerializedData<out VALUE, out DATA> : Message<VALUE> {

        val data: DATA
    }
}

val Message<*>.isOriginating: Boolean get() = context.isOriginating

fun Message<*>.descendsFromMessageWithId(messageId: ReceivedMessage.Id) = context.parentMessageId == messageId
fun Message<*>.descendsFrom(message: ReceivedMessage<*>) = descendsFromMessageWithId(message.id)

fun Message<*>.originatesFromMessageWithId(messageId: ReceivedMessage.Id) = context.originatingMessageId == messageId
fun Message<*>.originatesFrom(message: ReceivedMessage<*>) = originatesFromMessageWithId(message.id)

fun Message<*>.getProperty(propertyKey: String): String = getPropertyOrNull(propertyKey) ?: error("No value for property with key $propertyKey")