package org.sollecitom.chassis.messaging.test.utils

import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.utils.RandomGenerator
import org.sollecitom.chassis.kotlin.extensions.text.CharacterGroups
import org.sollecitom.chassis.kotlin.extensions.text.strings
import org.sollecitom.chassis.messaging.domain.TenantAgnosticTopic
import org.sollecitom.chassis.messaging.domain.Topic

context(RandomGenerator)
fun Topic.Companion.create(persistent: Boolean = true, tenant: Name = randomName(), namespaceName: Name = randomName(), namespace: Topic.Namespace? = Topic.Namespace(tenant = tenant, name = namespaceName), name: Name = randomName()): Topic = of(persistent, namespace, name)

context(RandomGenerator)
fun TenantAgnosticTopic.Companion.create(persistent: Boolean = true, name: Name = randomName()) = TenantAgnosticTopic(name, persistent)

context(RandomGenerator)
private fun randomName(): Name = random.strings(5..10, CharacterGroups.lowercaseCaseLetters).iterator().next().let(::Name)