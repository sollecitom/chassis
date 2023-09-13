package org.sollecitom.chassis.ddd.stubs.serialization.json.event

import org.json.JSONObject
import org.sollecitom.chassis.ddd.serialization.json.event.EventJsonSerdeSupport
import org.sollecitom.chassis.ddd.test.stubs.TestEntityEvent
import org.sollecitom.chassis.ddd.test.stubs.TestEvent
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object TestEntityEventJsonSerde : JsonSerde.SchemaAware<TestEntityEvent>, EventJsonSerdeSupport.EntitySpecific<TestEntityEvent> {

    private const val SCHEMA_LOCATION = "ddd/stubs/TestEntityEvent.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: TestEntityEvent) = JSONObject().apply {
        setEventFields(value)
    }

    override fun deserialize(json: JSONObject): TestEntityEvent {

        val (id, timestamp, type, context, entityId) = json.getEventFields()
        check(type == TestEntityEvent.type) { "Invalid event type $type for TestEntityEvent JSON serde" }
        return TestEntityEvent(entityId, id, timestamp, context)
    }
}

val TestEntityEvent.Companion.jsonSerde: JsonSerde.SchemaAware<TestEntityEvent> get() = TestEntityEventJsonSerde