package com.element.dpg.libs.chassis.ddd.stubs.serialization.json.event

import com.element.dpg.libs.chassis.ddd.serialization.json.event.EventJsonSerdeSupport
import com.element.dpg.libs.chassis.ddd.test.stubs.TestEvent
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import org.json.JSONObject

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