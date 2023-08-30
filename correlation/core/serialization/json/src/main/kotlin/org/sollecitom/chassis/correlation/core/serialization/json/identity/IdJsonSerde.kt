package org.sollecitom.chassis.correlation.core.serialization.json.identity

import com.github.erosb.jsonsKema.Schema
import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.identity.ULID
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde

// TODO move (along with the json file and the tests) to another module
private object IdJsonSerde : JsonSerde.SchemaAware<Id> {

    private const val TYPE_ULID = "ulid"
    private const val TYPE_STRING = "string"

    override val schema: Schema by lazy { jsonSchemaAt("correlation/identity/Id.json") }

    override fun serialize(value: Id) = JSONObject().apply {
        put(Fields.VALUE, value.stringValue)
        val type = when (value) {
            is ULID -> TYPE_ULID
            is StringId -> TYPE_STRING
        }
        put(Fields.TYPE, type)
    }

    override fun deserialize(json: JSONObject): Id {

        val type = json.getRequiredString(Fields.TYPE)
        val value = json.getRequiredString(Fields.VALUE)
        return when (type) {
            TYPE_ULID -> ULID(value)
            TYPE_STRING -> StringId(value)
            else -> error("Unknown ID type $type")
        }
    }

    private object Fields {
        const val TYPE = "type"
        const val VALUE = "value"
    }
}

val Id.Companion.jsonSerde: JsonSerde.SchemaAware<Id> get() = IdJsonSerde