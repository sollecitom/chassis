package org.sollecitom.chassis.example.event.serialization.json

import org.json.JSONObject
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.ddd.domain.CommandWasReceived
import org.sollecitom.chassis.ddd.domain.GenericCommandWasReceived
import org.sollecitom.chassis.ddd.domain.Happening
import org.sollecitom.chassis.ddd.serialization.json.event.EventJsonSerdeSupport
import org.sollecitom.chassis.ddd.serialization.json.happening.jsonSerde
import org.sollecitom.chassis.example.event.domain.user.registration.RegisterUser
import org.sollecitom.chassis.json.utils.jsonSchemaAt
import org.sollecitom.chassis.json.utils.serde.JsonSerde
import org.sollecitom.chassis.json.utils.serde.getValue
import org.sollecitom.chassis.json.utils.serde.setValue

private object CommandWasReceivedJsonSerde : JsonSerde.SchemaAware<CommandWasReceived<Command<*, *>>>, EventJsonSerdeSupport<CommandWasReceived<Command<*, *>>> {

    private const val SCHEMA_LOCATION = "example/event/domain/CommandWasReceived.json"
    override val schema by lazy { jsonSchemaAt(SCHEMA_LOCATION) }

    override fun serialize(value: CommandWasReceived<Command<*, *>>): JSONObject {

        val json = JSONObject().apply { setEventFields(value) }.setValue(Fields.COMMAND_TYPE, value.commandType, Happening.Type.jsonSerde)
        val serializedCommand: JSONObject = when (val command = value.command) {
            is RegisterUser -> {
                RegisterUserJsonSerde.serialize(command)
            }

            else -> error("Unknown command type ${command.type}")
        }
        return json.put(Fields.COMMAND, serializedCommand)
    }

    override fun deserialize(json: JSONObject): CommandWasReceived<Command<*, *>> {

        val (id, timestamp, type, context) = json.getEventFields()
        if (type != CommandWasReceived.type) error("Cannot deserialize event with type ${type}. Only ${CommandWasReceived.type} is supported.")
        val commandType = json.getValue(Fields.COMMAND_TYPE, Happening.Type.jsonSerde)
        val serializedCommand = json.getJSONObject(Fields.COMMAND)
        return when {
            commandType == RegisterUser.type -> {
                val command = RegisterUserJsonSerde.deserialize(serializedCommand)
                GenericCommandWasReceived(command, id, timestamp, context)
            }

            else -> error("Unsupported event type $commandType")
        }
    }

    private object Fields {
        const val COMMAND_TYPE = "command-type"
        const val COMMAND = "command"
    }
}

val CommandWasReceived.Companion.jsonSerde: JsonSerde.SchemaAware<CommandWasReceived<Command<*, *>>> get() = CommandWasReceivedJsonSerde