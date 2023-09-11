package org.sollecitom.chassis.pulsar.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator

@TestInstance(PER_CLASS)
private class PulsarTopicTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `parsing a persistent topic`() {

        val protocol = "persistent"
        val tenant = "a-tenant"
        val namespace = "a-namespace"
        val name = "a-name"
        val fullName = PulsarTopic.fullRawName(protocol, tenant, namespace, name)

        val topic = PulsarTopic.parse(fullName)

        assertThat(topic.persistent).isTrue()
        assertThat(topic).isInstanceOf<PulsarTopic.Persistent>()
        assertThat(topic.protocol).isEqualTo(protocol)
        assertThat(topic.tenant).isEqualTo(tenant)
        assertThat(topic.namespace).isEqualTo(namespace)
        assertThat(topic.name).isEqualTo(name)
        assertThat(topic.fullName).isEqualTo(fullName)
    }

    @Test
    fun `parsing a non-persistent topic`() {

        val protocol = "non-persistent"
        val tenant = "another-tenant"
        val namespace = "another-namespace"
        val name = "another-name"
        val fullName = PulsarTopic.fullRawName(protocol, tenant, namespace, name)

        val topic = PulsarTopic.parse(fullName)

        assertThat(topic.persistent).isFalse()
        assertThat(topic).isInstanceOf<PulsarTopic.NonPersistent>()
        assertThat(topic.protocol).isEqualTo(protocol)
        assertThat(topic.tenant).isEqualTo(tenant)
        assertThat(topic.namespace).isEqualTo(namespace)
        assertThat(topic.name).isEqualTo(name)
        assertThat(topic.fullName).isEqualTo(fullName)
    }
}