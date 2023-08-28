package org.sollecitom.chassis.json.utils.serde

import com.github.erosb.jsonsKema.Schema

interface JsonSerde<VALUE : Any> : JsonSerializer<VALUE>, JsonDeserializer<VALUE> {

    interface SchemaAware<VALUE : Any> : JsonSerde<VALUE> {

        val schema: Schema
    }
}