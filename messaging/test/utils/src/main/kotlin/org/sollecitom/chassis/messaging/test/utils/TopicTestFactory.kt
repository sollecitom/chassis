package org.sollecitom.chassis.messaging.test.utils

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.RandomGenerator
import org.sollecitom.chassis.kotlin.extensions.text.CharacterGroups
import org.sollecitom.chassis.kotlin.extensions.text.strings
import org.sollecitom.chassis.messaging.domain.Topic

context(RandomGenerator)
fun Topic.Companion.create(persistent: Boolean = true, namespace: Topic.Namespace? = Topic.Namespace(tenant = randomName(), name = randomName()), name: Name = randomName()): Topic = of(persistent, namespace, name)

context(RandomGenerator)
private fun randomName(): Name = random.strings(5..10, CharacterGroups.lowercaseCaseLetters).iterator().next().let(::Name)