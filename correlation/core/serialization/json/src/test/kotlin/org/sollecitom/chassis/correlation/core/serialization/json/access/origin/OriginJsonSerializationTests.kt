package org.sollecitom.chassis.correlation.core.serialization.json.access.origin

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin
import org.sollecitom.chassis.correlation.core.test.utils.access.origin.create
import org.sollecitom.chassis.json.test.utils.JsonSerdeTestSpecification

@TestInstance(PER_CLASS)
private class OriginJsonSerializationTests : JsonSerdeTestSpecification<Origin>, WithCoreGenerators by WithCoreGenerators.testProvider {

    override fun values() = listOf(Origin.create())

    override val jsonSerde get() = Origin.jsonSerde
}
