package com.element.dpg.libs.chassis.messaging.domain

import com.element.dpg.libs.chassis.ddd.domain.Event

interface EventStream<EVENT : Event> : MessageStream<EVENT> {

    suspend fun publish(event: EVENT, context: Message.Context = Message.Context()): Message.Id

    companion object
}

suspend fun <EVENT : Event> EventStream<EVENT>.publish(event: EVENT, parentMessage: ReceivedMessage<EVENT>) = publish(event, parentMessage.forkContext())