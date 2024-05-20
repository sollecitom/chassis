package com.element.dpg.libs.chassis.messaging.domain

interface Message<out VALUE> {

    val key: String
    val value: VALUE
    val properties: Map<String, String>
    val context: Context

    fun hasProperty(propertyKey: String): Boolean = properties.containsKey(propertyKey)
    fun getPropertyOrNull(propertyKey: String): String? = properties[propertyKey]

    interface Id : Comparable<Id> {

        val topic: Topic
        val partition: Topic.Partition?

        companion object
    }

    data class Context(val parentMessageId: Id? = null, val originatingMessageId: Id? = null) {

        init {
            require(parentMessageId == null && originatingMessageId == null || parentMessageId != null && originatingMessageId != null) { "Parent and originating message IDs must both be either specified or omitted" }
        }

        fun fork(parentMessageId: Id) = copy(parentMessageId = parentMessageId, originatingMessageId = originatingMessageId ?: parentMessageId)

        val isOriginating: Boolean get() = originatingMessageId == null

        companion object
    }
}

val Message<*>.isOriginating: Boolean get() = context.isOriginating

fun Message<*>.descendsFromMessageWithId(messageId: Message.Id) = context.parentMessageId == messageId
fun Message<*>.descendsFrom(message: ReceivedMessage<*>) = descendsFromMessageWithId(message.id)

fun Message<*>.originatesFromMessageWithId(messageId: Message.Id) = context.originatingMessageId == messageId
fun Message<*>.originatesFrom(message: ReceivedMessage<*>) = originatesFromMessageWithId(message.id)

fun Message<*>.getProperty(propertyKey: String): String = getPropertyOrNull(propertyKey) ?: error("No value for property with key $propertyKey")