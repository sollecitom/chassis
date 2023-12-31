package org.sollecitom.chassis.ddd.stubs.serialization.json.event

import org.json.JSONObject
import org.sollecitom.chassis.ddd.serialization.json.event.EventJsonSerdeSupport
import org.sollecitom.chassis.ddd.test.stubs.TestEvent
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object TestEventJsonSerde : JsonSerde.SchemaAware<TestEvent>, EventJsonSerdeSupport<TestEvent> {

    private const val SCHEMA_LOCATION = "ddd/stubs/TestEvent.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: TestEvent) = JSONObject().apply {
        setEventFields(value)
    }

    override fun deserialize(json: JSONObject): TestEvent {

        val (id, timestamp, type, context) = json.getEventFields()
        check(type == TestEvent.type) { "Invalid event type $type for TestEvent JSON serde" }
        return TestEvent(id, timestamp, context)
    }
}

val TestEvent.Companion.jsonSerde: JsonSerde.SchemaAware<TestEvent> get() = TestEventJsonSerde