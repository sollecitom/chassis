package org.sollecitom.chassis.pulsar.test.utils

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.random
import org.sollecitom.chassis.core.utils.RandomGenerator
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import org.sollecitom.chassis.pulsar.utils.TenantAgnosticPulsarTopic

// TODO remove all of these
context(RandomGenerator)
fun PulsarTopic.Companion.create(persistent: Boolean = true, tenant: Name = Name.random(), namespaceName: Name = Name.random(), name: Name = Name.random(), namespace: PulsarTopic.Namespace? = PulsarTopic.Namespace(tenant = tenant, name = namespaceName)): PulsarTopic = of(persistent, namespace, name)

context(RandomGenerator)
fun TenantAgnosticPulsarTopic.Companion.create(persistent: Boolean = true, name: Name = Name.random()) = TenantAgnosticPulsarTopic(name, persistent)