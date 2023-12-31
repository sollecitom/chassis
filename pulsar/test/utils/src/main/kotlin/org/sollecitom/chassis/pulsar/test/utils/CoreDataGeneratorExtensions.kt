package org.sollecitom.chassis.pulsar.test.utils

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.RandomGenerator
import org.sollecitom.chassis.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters
import org.sollecitom.chassis.kotlin.extensions.text.strings
import org.sollecitom.chassis.pulsar.utils.PulsarTopic

context(RandomGenerator)
fun PulsarTopic.Companion.create(persistent: Boolean = true, namespace: PulsarTopic.Namespace? = PulsarTopic.Namespace(tenant = randomName(), name = randomName()), name: Name = randomName()): PulsarTopic = of(persistent, namespace, name)

context(RandomGenerator)
private fun randomName(): Name = random.strings(5..10, lowercaseCaseLetters).iterator().next().let(::Name)