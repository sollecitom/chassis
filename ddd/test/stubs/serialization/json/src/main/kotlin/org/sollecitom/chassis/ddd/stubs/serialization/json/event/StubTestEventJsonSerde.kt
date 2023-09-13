package org.sollecitom.chassis.ddd.stubs.serialization.json.event

import org.json.JSONObject
import org.sollecitom.chassis.ddd.domain.Event
import org.sollecitom.chassis.ddd.domain.Happening
import org.sollecitom.chassis.ddd.serialization.json.happening.jsonSerde
import org.sollecitom.chassis.ddd.test.stubs.TestEntityEvent
import org.sollecitom.chassis.ddd.test.stubs.TestEvent
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue

private object StubTestEventJsonSerde : JsonSerde.SchemaAware<Event> {

    private const val SCHEMA_LOCATION = "ddd/stubs/GenericTestEvent.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Event) = when (value) {
        is TestEvent -> TestEvent.jsonSerde.serialize(value)
        is TestEntityEvent -> TestEntityEvent.jsonSerde.serialize(value)
        else -> error("Unsupported test event stub type ${value.type}")
    }

    override fun deserialize(json: JSONObject) = when (val type = json.getValue(Fields.TYPE, Happening.Type.jsonSerde)) {
        TestEvent.type -> TestEvent.jsonSerde.deserialize(json)
        TestEntityEvent.type -> TestEntityEvent.jsonSerde.deserialize(json)
        else -> error("Unsupported test event stub type $type")
    }

    private object Fields {
        const val TYPE = "type"
    }
}

val Event.Companion.testStubJsonSerde: JsonSerde.SchemaAware<Event> get() = StubTestEventJsonSerde