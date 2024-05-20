package org.sollecitom.chassis.pulsar.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator

@TestInstance(PER_CLASS)
private class PulsarTopicTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `parsing a persistent topic`() {

        val protocol = "persistent".let(::Name)
        val namespace = PulsarTopic.Namespace(tenant = "a-tenant".let(::Name), name = "a-namespace".let(::Name))
        val name = "a-name".let(::Name)
        val fullName = PulsarTopic.fullRawName(protocol, namespace, name)

        val topic = PulsarTopic.parse(fullName.value)

        assertThat(topic.persistent).isTrue()
        assertThat(topic).isInstanceOf<PulsarTopic.Persistent>()
        assertThat(topic.protocol).isEqualTo(protocol)
        assertThat(topic.namespace).isEqualTo(namespace)
        assertThat(topic.name).isEqualTo(name)
        assertThat(topic.fullName).isEqualTo(fullName)
    }

    @Test
    fun `parsing a non-persistent topic`() {

        val protocol = "non-persistent".let(::Name)
        val namespace = PulsarTopic.Namespace(tenant = "another-tenant".let(::Name), name = "another-namespace".let(::Name))
        val name = "another-name".let(::Name)
        val fullName = PulsarTopic.fullRawName(protocol, namespace, name)

        val topic = PulsarTopic.parse(fullName.value)

        assertThat(topic.persistent).isFalse()
        assertThat(topic).isInstanceOf<PulsarTopic.NonPersistent>()
        assertThat(topic.protocol).isEqualTo(protocol)
        assertThat(topic.namespace).isEqualTo(namespace)
        assertThat(topic.name).isEqualTo(name)
        assertThat(topic.fullName).isEqualTo(fullName)
    }
}