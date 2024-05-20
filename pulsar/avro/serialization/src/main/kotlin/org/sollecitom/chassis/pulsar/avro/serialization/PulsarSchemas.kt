package org.sollecitom.chassis.pulsar.avro.serialization

import org.apache.pulsar.client.api.Schema
import org.apache.pulsar.client.api.schema.SchemaDefinition
import org.apache.pulsar.client.impl.schema.AvroSchema
import com.element.dpg.libs.chassis.avro.serialization.utils.RecordSerde

fun <VALUE : Any> RecordSerde<VALUE>.asPulsarSchema(): AvroSchema<VALUE> = PulsarSchemas.forSerde(this)

private object PulsarSchemas {

    fun <VALUE : Any> forSerde(serde: RecordSerde<VALUE>): AvroSchema<VALUE> = createSchema(serde)

    private fun <VALUE : Any> createSchema(serde: RecordSerde<VALUE>): AvroSchema<VALUE> {

        val writer = AvroWriter(serde)
        val reader = AvroReader(serde)
        val definition = SchemaDefinition.builder<VALUE>().withSchemaWriter(writer).withSchemaReader(reader).withJsonDef(serde.schema.toString()).build()
        return Schema.AVRO(definition) as AvroSchema<VALUE>
    }
}