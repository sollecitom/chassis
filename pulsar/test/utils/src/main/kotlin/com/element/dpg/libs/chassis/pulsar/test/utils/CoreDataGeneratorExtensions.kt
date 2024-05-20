package com.element.dpg.libs.chassis.pulsar.test.utils

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.random
import com.element.dpg.libs.chassis.core.utils.RandomGenerator
import com.element.dpg.libs.chassis.pulsar.utils.PulsarTopic
import com.element.dpg.libs.chassis.pulsar.utils.TenantAgnosticPulsarTopic

// TODO remove all of these
context(RandomGenerator)
fun PulsarTopic.Companion.create(persistent: Boolean = true, tenant: Name = Name.random(), namespaceName: Name = Name.random(), name: Name = Name.random(), namespace: PulsarTopic.Namespace? = PulsarTopic.Namespace(tenant = tenant, name = namespaceName)): PulsarTopic = of(persistent, namespace, name)

context(RandomGenerator)
fun TenantAgnosticPulsarTopic.Companion.create(persistent: Boolean = true, name: Name = Name.random()) = TenantAgnosticPulsarTopic(name, persistent)