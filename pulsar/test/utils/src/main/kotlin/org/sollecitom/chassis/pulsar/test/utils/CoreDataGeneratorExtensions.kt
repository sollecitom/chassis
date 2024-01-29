package org.sollecitom.chassis.pulsar.test.utils

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.RandomGenerator
import org.sollecitom.chassis.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters
import org.sollecitom.chassis.kotlin.extensions.text.strings
import org.sollecitom.chassis.pulsar.utils.PulsarTopic
import org.sollecitom.chassis.pulsar.utils.TenantAgnosticPulsarTopic

context(RandomGenerator)
fun PulsarTopic.Companion.create(persistent: Boolean = true, tenant: Name = randomName(), namespaceName: Name = randomName(), name: Name = randomName(), namespace: PulsarTopic.Namespace? = PulsarTopic.Namespace(tenant = tenant, name = namespaceName)): PulsarTopic = of(persistent, namespace, name)

context(RandomGenerator)
fun TenantAgnosticPulsarTopic.Companion.create(persistent: Boolean = true, name: Name = randomName()) = TenantAgnosticPulsarTopic(name, persistent)

context(RandomGenerator)
private fun randomName(): Name = random.strings(5..10, lowercaseCaseLetters).iterator().next().let(::Name)