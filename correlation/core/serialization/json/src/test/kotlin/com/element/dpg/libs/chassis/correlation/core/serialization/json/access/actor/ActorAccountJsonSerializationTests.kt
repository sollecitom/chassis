package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.actor

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.actor.service
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.actor.user
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import java.util.*

@TestInstance(PER_CLASS)
private class ActorAccountJsonSerializationTests : JsonSerdeTestSpecification<Actor.Account>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Actor.Account.jsonSerde

    override fun parameterizedArguments() = listOf(
        "user" to Actor.Account.user(),
        "user-with-explicit-locale" to Actor.Account.user(locale = Locale.ITALY),
        "service" to Actor.Account.service()
    )
}