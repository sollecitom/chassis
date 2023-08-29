package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.service
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.user
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification
import org.sollecitom.chassis.test.utils.params.ParameterizedTestSupport

@TestInstance(PER_CLASS)
private class ActorAccountJsonSerializationTests : JsonSerdeTestSpecification<Actor.Account>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override val jsonSerde get() = Actor.Account.jsonSerde

    override fun arguments() = ParameterizedTestSupport.arguments(
        "user" to Actor.Account.user(),
        "service" to Actor.Account.service(),
    )
}