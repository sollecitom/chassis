package org.sollecitom.chassis.pulsar.test.utils

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters
import org.sollecitom.chassis.kotlin.extensions.text.strings
import org.sollecitom.chassis.pulsar.utils.PulsarTopic

context(CoreDataGenerator)
fun PulsarTopic.Companion.create(persistent: Boolean = true, tenant: Name? = randomName(), namespace: Name? = randomName(), name: Name = randomName()): PulsarTopic = of(persistent, tenant, namespace, name)

context(CoreDataGenerator)
private fun randomName(): Name = random.strings(5..10, lowercaseCaseLetters).iterator().next().let(::Name)