package org.sollecitom.chassis.json.utils.serde

interface JsonSerde<VALUE : Any> : JsonSerializer<VALUE>, JsonDeserializer<VALUE>