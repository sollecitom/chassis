package com.element.dpg.libs.chassis.pulsar.messaing.test.utils

import com.element.dpg.libs.chassis.messaging.domain.Topic
import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.ProducerBuilder

fun <V> ConsumerBuilder<V>.topics(vararg topics: Topic): ConsumerBuilder<V> = topic(*topics.map { it.fullName.value }.toTypedArray())

fun <V> ProducerBuilder<V>.topic(topic: Topic): ProducerBuilder<V> = topic(topic.fullName.value)