package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.actor

import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.actor.Actor
import com.element.dpg.libs.chassis.correlation.core.domain.access.actor.impersonating
import com.element.dpg.libs.chassis.correlation.core.domain.access.actor.onBehalfOf
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.actor.create
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.actor.direct
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class ActorJsonSerializationTests : JsonSerdeTestSpecification<Actor>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Actor.jsonSerde

    override fun parameterizedArguments() = listOf(
        "direct" to Actor.direct(),
        "on-behalf" to Actor.direct().onBehalfOf(Actor.UserAccount.create()),
        "impersonating" to Actor.direct().impersonating(Actor.UserAccount.create())
    )
}