package org.sollecitom.chassis.correlation.core.serialization.json.trace

import com.github.erosb.jsonsKema.Schema
import kotlinx.datetime.Instant
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.correlation.core.domain.trace.InvocationTrace
import org.sollecitom.chassis.correlation.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

private object InvocationTraceJsonSerde : JsonSerde.SchemaAware<InvocationTrace> {

    override val schema: Schema by lazy { jsonSchemaAt("trace/InvocationTrace.json") }

    override fun serialize(value: InvocationTrace) = JSONObject().apply {
        put(Fields.ID, Id.jsonSerde.serialize(value.id))
        put(Fields.CREATED_AT, value.createdAt.toString())
    }

    override fun deserialize(json: JSONObject): InvocationTrace {

        val id = json.getRequiredJSONObject(Fields.ID).let(Id.jsonSerde::deserialize)
        val createdAt = json.getRequiredString(Fields.CREATED_AT).let(Instant::parse)
        return InvocationTrace(id = id, createdAt = createdAt)
    }

    private object Fields {
        const val ID = "id"
        const val CREATED_AT = "created-at"
    }
}

val InvocationTrace.Companion.jsonSerde: JsonSerde.SchemaAware<InvocationTrace> get() = InvocationTraceJsonSerde