package org.sollecitom.chassis.pulsar.utils

import org.apache.pulsar.client.admin.PulsarAdmin
import org.apache.pulsar.client.admin.PulsarAdminException
import org.apache.pulsar.common.policies.data.AutoTopicCreationOverride
import org.apache.pulsar.common.policies.data.SchemaCompatibilityStrategy
import org.apache.pulsar.common.policies.data.TenantInfo
import org.apache.pulsar.common.policies.data.TopicType

fun PulsarAdmin.createTopic(fullyQualifiedTopic: String, numberOfPartitions: Int = 1) = topics().createPartitionedTopic(fullyQualifiedTopic, numberOfPartitions)

fun PulsarAdmin.createTenant(tenant: String) = tenants().createTenant(tenant, TenantInfo.builder().allowedClusters(clusters().clusters.toSet()).build())

fun PulsarAdmin.createNamespace(tenant: String, namespace: String) = namespaces().createNamespace("$tenant/$namespace")

fun PulsarAdmin.configureNamespace(tenant: String, namespace: String, allowTopicCreation: Boolean = false, isAllowAutoUpdateSchema: Boolean = false, schemaValidationEnforced: Boolean = true, schemaCompatibilityStrategy: SchemaCompatibilityStrategy = SchemaCompatibilityStrategy.FULL_TRANSITIVE) {

    val tenantNamespace = "$tenant/$namespace"
    namespaces().setAutoTopicCreation(tenantNamespace, AutoTopicCreationOverride.builder().allowAutoTopicCreation(allowTopicCreation).topicType(TopicType.PARTITIONED.name).build())
    namespaces().setAutoTopicCreation(tenantNamespace, AutoTopicCreationOverride.builder().allowAutoTopicCreation(allowTopicCreation).topicType(TopicType.NON_PARTITIONED.name).build())
    namespaces().setIsAllowAutoUpdateSchema(tenantNamespace, isAllowAutoUpdateSchema)
    namespaces().setSchemaValidationEnforced(tenantNamespace, schemaValidationEnforced)
    namespaces().setSchemaCompatibilityStrategy(tenantNamespace, schemaCompatibilityStrategy)
}

fun PulsarAdmin.createTenantAndNamespace(tenantId: String, namespace: String, allowTopicCreation: Boolean = false, isAllowAutoUpdateSchema: Boolean = false, schemaValidationEnforced: Boolean = true, schemaCompatibilityStrategy: SchemaCompatibilityStrategy = SchemaCompatibilityStrategy.FULL_TRANSITIVE) {

    createTenant(tenantId)
    createNamespace(tenantId, namespace)
    configureNamespace(tenantId, namespace, allowTopicCreation, isAllowAutoUpdateSchema, schemaValidationEnforced, schemaCompatibilityStrategy)
}

fun PulsarAdmin.ensureTenantAndNamespaceExist(tenantId: String, namespace: String, allowTopicCreation: Boolean = false, isAllowAutoUpdateSchema: Boolean = false, schemaValidationEnforced: Boolean = true, schemaCompatibilityStrategy: SchemaCompatibilityStrategy = SchemaCompatibilityStrategy.FULL_TRANSITIVE) {

    ensureTenantExists(tenantId)
    val newNamespaceWasCreated = ensureNamespaceExists(tenantId, namespace)
    if (newNamespaceWasCreated) {
        configureNamespace(tenantId, namespace, allowTopicCreation, isAllowAutoUpdateSchema, schemaValidationEnforced, schemaCompatibilityStrategy)
    }
}

fun PulsarAdmin.ensureTenantExists(tenantId: String): Boolean = try {
    createTenant(tenantId)
    true
} catch (error: PulsarAdminException.ConflictException) {
    false
}

fun PulsarAdmin.ensureNamespaceExists(tenantId: String, namespace: String): Boolean = try {
    createNamespace(tenantId, namespace)
    true
} catch (error: PulsarAdminException.ConflictException) {
    false
}

fun PulsarAdmin.ensureTopicExists(fullyQualifiedTopic: String, numberOfPartitions: Int = 1): Boolean = try {
    createTopic(fullyQualifiedTopic, numberOfPartitions)
    true
} catch (error: PulsarAdminException.ConflictException) {
    false
}