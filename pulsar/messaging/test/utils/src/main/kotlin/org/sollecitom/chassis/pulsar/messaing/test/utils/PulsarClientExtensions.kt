package org.sollecitom.chassis.pulsar.messaing.test.utils

import org.apache.pulsar.client.api.ConsumerBuilder
import org.apache.pulsar.client.api.ProducerBuilder
import org.sollecitom.chassis.messaging.domain.Topic

fun <V> ConsumerBuilder<V>.topics(vararg topics: Topic): ConsumerBuilder<V> = topic(*topics.map { it.fullName.value }.toTypedArray())

fun <V> ProducerBuilder<V>.topic(topic: Topic): ProducerBuilder<V> = topic(topic.fullName.value)