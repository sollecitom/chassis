package com.element.dpg.libs.chassis.ddd.stubs.serialization.json.event

import org.json.JSONObject
import com.element.dpg.libs.chassis.ddd.serialization.json.event.EventJsonSerdeSupport
import com.element.dpg.libs.chassis.ddd.test.stubs.TestEntityEvent
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde

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