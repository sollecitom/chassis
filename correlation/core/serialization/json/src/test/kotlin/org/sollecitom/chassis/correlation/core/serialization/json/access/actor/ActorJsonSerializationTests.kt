package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.impersonating
import org.sollecitom.chassis.correlation.core.domain.access.actor.onBehalfOf
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.create
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.direct
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification
import org.sollecitom.chassis.json.test.utils.compliesWith
import org.sollecitom.chassis.test.utils.params.ParameterizedTestSupport

@TestInstance(PER_CLASS)
private class ActorJsonSerializationTests : JsonSerdeTestSpecification<Actor>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override val jsonSerde get() = Actor.jsonSerde

    override fun arguments() = ParameterizedTestSupport.arguments(
//        "direct" to Actor.direct(),
//        "on-behalf" to Actor.direct().onBehalfOf(Actor.UserAccount.create()),
        "impersonating" to Actor.direct().impersonating(Actor.UserAccount.create()),
    )

    // TODO remove
    @ParameterizedTest
    @MethodSource("arguments")
    override fun `serializing and deserializing to and from JSON`(argument: ParameterizedTestSupport.InlineWrapper<Actor>) {

        val value = argument.value
        val json = jsonSerde.serialize(value)
        val deserialized = jsonSerde.deserialize(json)

        assertThat(deserialized).isEqualTo(value)
        assertThat(json).compliesWith(jsonSerde.schema)
    }
}