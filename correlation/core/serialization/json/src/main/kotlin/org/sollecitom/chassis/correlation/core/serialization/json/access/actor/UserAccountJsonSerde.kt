package org.sollecitom.chassis.correlation.core.serialization.json.access.actor

import org.json.JSONObject
import org.sollecitom.chassis.core.domain.identity.Id
import com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde
import org.sollecitom.chassis.correlation.core.domain.access.actor.Actor
import org.sollecitom.chassis.correlation.core.serialization.json.customer.jsonSerde
import org.sollecitom.chassis.correlation.core.serialization.json.tenancy.jsonSerde
import org.sollecitom.chassis.json.utils.getRequiredString
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue
import java.util.*

internal object UserAccountJsonSerde : JsonSerde.SchemaAware<Actor.UserAccount> {

    const val TYPE_VALUE = "user"
    private const val SCHEMA_LOCATION = "correlation/access/actor/UserAccount.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: Actor.UserAccount) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ID, value.id, Id.jsonSerde)
        put(Fields.LOCALE, value.locale.toLanguageTag())
        setValue(Fields.CUSTOMER, value.customer, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
        setValue(Fields.TENANT, value.tenant, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
    }

    override fun deserialize(json: JSONObject): Actor.UserAccount {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val id = json.getValue(Fields.ID, Id.jsonSerde)
        val locale = json.getRequiredString(Fields.LOCALE).let(Locale::forLanguageTag)
        val customer = json.getValue(Fields.CUSTOMER, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
        val tenant = json.getValue(Fields.TENANT, com.element.dpg.libs.chassis.core.serialization.json.identity.jsonSerde)
        return Actor.UserAccount(id = id, locale = locale, customer = customer, tenant = tenant)
    }

    private object Fields {
        const val TYPE = "type"
        const val ID = "id"
        const val LOCALE = "locale"
        const val CUSTOMER = "customer"
        const val TENANT = "tenant"
    }
}

val Actor.UserAccount.Companion.jsonSerde: JsonSerde.SchemaAware<Actor.UserAccount> get() = UserAccountJsonSerde