package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.domain.access.actor.impersonating
import org.sollecitom.chassis.correlation.core.domain.access.actor.onBehalfOf
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.create
import org.sollecitom.chassis.correlation.core.test.utils.access.actor.direct
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class ActorJsonSerializationTests : JsonSerdeTestSpecification<Actor>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Actor.jsonSerde

    override fun parameterizedArguments() = listOf(
        "direct" to Actor.direct(),
        "on-behalf" to Actor.direct().onBehalfOf(Actor.UserAccount.create()),
        "impersonating" to Actor.direct().impersonating(Actor.UserAccount.create())
    )
}