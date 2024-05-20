package com.element.dpg.libs.chassis.json.utils.serde.serde

import com.element.dpg.libs.chassis.json.utils.serde.Schema

interface JsonSerde<VALUE : Any> : JsonSerializer<VALUE>, JsonDeserializer<VALUE> {

    interface SchemaAware<VALUE : Any> : JsonSerde<VALUE> {

        val schema: Schema
    }
}