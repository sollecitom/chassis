package org.sollecitom.chassis.messaging.test.utils

import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.random
import com.element.dpg.libs.chassis.core.utils.RandomGenerator
import org.sollecitom.chassis.messaging.domain.TenantAgnosticTopic
import org.sollecitom.chassis.messaging.domain.Topic

context(RandomGenerator)
fun Topic.Companion.create(persistent: Boolean = true, tenant: Name = Name.random(), namespaceName: Name = Name.random(), namespace: Topic.Namespace? = Topic.Namespace(tenant = tenant, name = namespaceName), name: Name = Name.random()): Topic = of(persistent, namespace, name)

context(RandomGenerator)
fun TenantAgnosticTopic.Companion.create(persistent: Boolean = true, namespaceName: Name = Name.random(), name: Name = Name.random()) = TenantAgnosticTopic(name = name, namespaceName = namespaceName, persistent = persistent)