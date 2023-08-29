package org.sollecitom.chassis.correlation.core.serialization.json.access.session

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.session.Session
import org.sollecitom.chassis.correlation.core.test.utils.access.session.federated
import org.sollecitom.chassis.correlation.core.test.utils.access.session.simple
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification
import org.sollecitom.chassis.test.utils.params.ParameterizedTestSupport

@TestInstance(PER_CLASS)
private class SessionJsonSerializationTests : JsonSerdeTestSpecification<Session>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override val jsonSerde get() = Session.jsonSerde

    override fun arguments() = ParameterizedTestSupport.arguments(
        "simple" to Session.simple(),
        "federated" to Session.federated(),
    )
}