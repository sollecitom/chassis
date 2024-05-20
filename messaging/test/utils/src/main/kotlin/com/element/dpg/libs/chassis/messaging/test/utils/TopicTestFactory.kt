package com.element.dpg.libs.chassis.messaging.test.utils

import com.element.dpg.libs.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.random
import com.element.dpg.libs.chassis.core.utils.RandomGenerator
import com.element.dpg.libs.chassis.messaging.domain.TenantAgnosticTopic
import com.element.dpg.libs.chassis.messaging.domain.Topic

context(RandomGenerator)
fun Topic.Companion.create(persistent: Boolean = true, tenant: Name = Name.random(), namespaceName: Name = Name.random(), namespace: Topic.Namespace? = Topic.Namespace(tenant = tenant, name = namespaceName), name: Name = Name.random()): Topic = of(persistent, namespace, name)

context(RandomGenerator)
fun TenantAgnosticTopic.Companion.create(persistent: Boolean = true, namespaceName: Name = Name.random(), name: Name = Name.random()) = TenantAgnosticTopic(name = name, namespaceName = namespaceName, persistent = persistent)