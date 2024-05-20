package com.element.dpg.libs.chassis.json.utils.serde

import com.element.dpg.libs.chassis.json.utils.Schema

interface JsonSerde<VALUE : Any> : JsonSerializer<VALUE>, JsonDeserializer<VALUE> {

    interface SchemaAware<VALUE : Any> : JsonSerde<VALUE> {

        val schema: Schema
    }
}