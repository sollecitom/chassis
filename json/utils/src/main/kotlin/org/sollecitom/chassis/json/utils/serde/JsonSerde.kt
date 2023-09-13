package org.sollecitom.chassis.json.utils.serde

import org.sollecitom.chassis.json.utils.Schema

interface JsonSerde<VALUE : Any> : JsonSerializer<VALUE>, JsonDeserializer<VALUE> {

    interface SchemaAware<VALUE : Any> : JsonSerde<VALUE> {

        val schema: Schema
    }
}