package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.session

import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.session.Session
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.session.federated
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.session.simple
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class SessionJsonSerializationTests : JsonSerdeTestSpecification<Session>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Session.jsonSerde

    override fun parameterizedArguments() = listOf(
        "simple" to Session.simple(),
        "federated" to Session.federated(),
    )
}