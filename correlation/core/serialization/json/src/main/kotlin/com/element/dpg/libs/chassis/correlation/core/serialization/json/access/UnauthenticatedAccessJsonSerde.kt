package com.element.dpg.libs.chassis.correlation.core.serialization.json.access

import com.element.dpg.libs.chassis.correlation.core.domain.access.authorization.AuthorizationPrincipal
import com.element.dpg.libs.chassis.correlation.core.domain.access.origin.Origin
import com.element.dpg.libs.chassis.correlation.core.domain.access.scope.AccessScope
import com.element.dpg.libs.chassis.correlation.core.serialization.json.access.autorization.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.serialization.json.access.origin.jsonSerde
import com.element.dpg.libs.chassis.correlation.core.serialization.json.access.scope.jsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.getRequiredString
import com.element.dpg.libs.chassis.json.utils.serde.jsonSchemaAt
import com.element.dpg.libs.chassis.json.utils.serde.serde.JsonSerde
import com.element.dpg.libs.chassis.json.utils.serde.serde.getValue
import com.element.dpg.libs.chassis.json.utils.serde.serde.setValue
import org.json.JSONObject

internal object UnauthenticatedAccessJsonSerde : JsonSerde.SchemaAware<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated> {

    const val TYPE_VALUE = "unauthenticated"
    private const val SCHEMA_LOCATION = "correlation/access/UnauthenticatedAccess.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated) = JSONObject().apply {
        put(Fields.TYPE, TYPE_VALUE)
        setValue(Fields.ORIGIN, value.origin, Origin.jsonSerde)
        setValue(Fields.AUTHORIZATION, value.authorization, AuthorizationPrincipal.jsonSerde)
        setValue(Fields.SCOPE, value.scope, AccessScope.jsonSerde)
    }

    override fun deserialize(json: JSONObject): _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated {

        val type = json.getRequiredString(Fields.TYPE)
        check(type == TYPE_VALUE) { "Invalid type '$type'. Must be '$TYPE_VALUE'" }
        val origin = json.getValue(Fields.ORIGIN, Origin.jsonSerde)
        val authorization = json.getValue(Fields.AUTHORIZATION, AuthorizationPrincipal.jsonSerde)
        val scope = json.getValue(Fields.SCOPE, AccessScope.jsonSerde)
        return _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated(origin = origin, authorization = authorization, scope = scope)
    }

    private object Fields {
        const val TYPE = "type"
        const val ORIGIN = "origin"
        const val AUTHORIZATION = "authorization"
        const val SCOPE = "scope"
    }
}

val _root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated.Companion.jsonSerde: JsonSerde.SchemaAware<_root_ide_package_.com.element.dpg.libs.chassis.correlation.core.domain.access.Access.Unauthenticated> get() = UnauthenticatedAccessJsonSerde