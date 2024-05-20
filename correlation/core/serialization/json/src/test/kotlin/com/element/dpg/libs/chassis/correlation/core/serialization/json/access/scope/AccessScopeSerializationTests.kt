package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.scope

import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.scope.AccessContainer
import com.element.dpg.libs.chassis.correlation.core.domain.access.scope.AccessScope
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.scope.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.scope.withContainerStack
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class AccessScopeJsonSerializationTests : JsonSerdeTestSpecification<AccessScope>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = AccessScope.jsonSerde

    override fun parameterizedArguments() = listOf(
        "empty" to AccessScope.withContainerStack(),
        "single-container" to AccessScope.withContainerStack(AccessContainer.create()),
        "multiple-containers" to AccessScope.withContainerStack(AccessContainer.create(), AccessContainer.create())
    )
}