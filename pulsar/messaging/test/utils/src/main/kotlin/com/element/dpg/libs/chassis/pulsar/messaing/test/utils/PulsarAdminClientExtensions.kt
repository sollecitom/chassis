package com.element.dpg.libs.chassis.pulsar.messaing.test.utils

import org.apache.pulsar.client.admin.PulsarAdmin
import org.apache.pulsar.common.policies.data.SchemaCompatibilityStrategy
import com.element.dpg.libs.chassis.messaging.domain.Topic
import com.element.dpg.libs.chassis.pulsar.utils.ensureTenantAndNamespaceExist
import com.element.dpg.libs.chassis.pulsar.utils.ensureTopicExists

fun PulsarAdmin.ensureTopicExists(topic: Topic, numberOfPartitions: Int = 1, allowTopicCreation: Boolean = false, isAllowAutoUpdateSchema: Boolean = false, schemaValidationEnforced: Boolean = true, schemaCompatibilityStrategy: SchemaCompatibilityStrategy = SchemaCompatibilityStrategy.FULL_TRANSITIVE) {

    topic.namespace?.let { ensureTenantAndNamespaceExist(it.tenant.value, it.name.value, allowTopicCreation, isAllowAutoUpdateSchema, schemaValidationEnforced, schemaCompatibilityStrategy) }
    ensureTopicExists(topic.fullName.value, numberOfPartitions)
}