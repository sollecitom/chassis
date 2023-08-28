package org.sollecitom.chassis.correlation.core.serialization.json

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.networking.IpAddress
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.WithCoreGenerators
import org.sollecitom.chassis.correlation.core.domain.origin.Origin
import org.sollecitom.chassis.correlation.core.test.utils.origin.create
import org.sollecitom.chassis.json.test.utils.compliesWith
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

@TestInstance(PER_CLASS)
private class OriginJsonSerializationTests : WithCoreGenerators by WithCoreGenerators.testProvider {

    @Test
    fun `serializing and deserializing origin to and from JSON`() {

        val origin = Origin.create()

        val json = Origin.jsonSerde.serialize(origin)
        val deserialized = Origin.jsonSerde.deserialize(json)

        assertThat(deserialized).isEqualTo(origin)
        assertThat(json).compliesWith(Origin.jsonSerde.schema)
    }
}
