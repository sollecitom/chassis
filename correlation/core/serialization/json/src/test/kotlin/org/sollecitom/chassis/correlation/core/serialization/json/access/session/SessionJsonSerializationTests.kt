package org.sollecitom.chassis.correlation.core.serialization.json.access.session

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.session.Session
import org.sollecitom.chassis.correlation.core.test.utils.access.session.federated
import org.sollecitom.chassis.correlation.core.test.utils.access.session.simple
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class SessionJsonSerializationTests : JsonSerdeTestSpecification<Session>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Session.jsonSerde

    override fun parameterizedArguments() = listOf(
        "simple" to Session.simple(),
        "federated" to Session.federated(),
    )
}