package org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.endpoints

import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.BiDiBodyLensSpec
import org.http4k.lens.BodyLensSpec
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.json.JSONObject
import org.sollecitom.chassis.ddd.domain.Command
import org.sollecitom.chassis.example.service.endpoint.write.adapters.driving.web.api.Endpoint
import org.sollecitom.chassis.example.service.endpoint.write.application.ApplicationCommand
import org.sollecitom.chassis.example.service.endpoint.write.application.user.RegisterUser
import org.sollecitom.chassis.http4k.server.utils.toSuspending
import org.sollecitom.chassis.http4k.utils.lens.jsonObject
import org.sollecitom.chassis.json.utils.getJSONObjectOrNull
import org.sollecitom.chassis.json.utils.getRequiredJSONObject
import org.sollecitom.chassis.logger.core.loggable.Loggable

sealed class RegisterUserCommandsEndpoint {

    class V1(private val handle: suspend (RegisterUser.V1) -> RegisterUser.V1.Result) : Endpoint {

        override val path = "/commands/${COMMAND_TYPE.id.value}/${COMMAND_TYPE.version.value}"
        override val methods = setOf(Method.POST)

        override val route = routes(
            acceptCommand()
        )

        private fun acceptCommand() = path bind Method.POST toSuspending { request ->

            logger.debug { "Received command with type $COMMAND_TYPE" }
            val command = command(request)

            val result = handle(command)

            result.toHttpResponse()
        }

        private fun RegisterUser.V1.Result.toHttpResponse(): Response = when (this) {
            RegisterUser.V1.Result.Accepted -> Response(Status.ACCEPTED)
            is RegisterUser.V1.Result.Rejected.EmailAddressAlreadyInUse -> Response(Status.UNPROCESSABLE_ENTITY).body("Email address already in use by another user") // TODO switch to JSON body
        }

        companion object : Loggable() {

            private val COMMAND_TYPE: Command.Type = RegisterUser.V1.Type
            private val command = Body.jsonObject().map(RegisterUserCommandDeserializer.V1).toLens()
        }
    }
}

private class ApplicationCommandJsonDeserializer(private val commandType: Command.Type) : JsonDeserializer<ApplicationCommand<*>> {

    override fun deserialize(json: JSONObject) = when (commandType) {
        RegisterUser.V1.Type -> RegisterUserCommandDeserializer.V1.deserialize(json)
        else -> error("Unknown command type $commandType")
    }
}

// TODO change
private object RegisterUserCommandDeserializer {

    internal object V1 : JsonDeserializer<RegisterUser.V1> {

        override fun deserialize(json: JSONObject): RegisterUser.V1 {

            TODO("")
        }
    }
}

//emailAddress: EmailAddress

fun <VALUE : Any> JSONObject.getValueOrNull(key: String, deserializer: JsonDeserializer<VALUE>): VALUE? = getJSONObjectOrNull(key)?.let(deserializer::deserialize)

fun <VALUE : Any> JSONObject.getValue(key: String, deserializer: JsonDeserializer<VALUE>): VALUE = getRequiredJSONObject(key).let(deserializer::deserialize)

// TODO move
fun <VALUE : Any> BiDiBodyLensSpec<JSONObject>.map(serde: JsonSerde<VALUE>): BiDiBodyLensSpec<VALUE> = map(serde::deserialize, serde::serialize)

fun <VALUE : Any> BodyLensSpec<JSONObject>.map(deserializer: JsonDeserializer<VALUE>): BodyLensSpec<VALUE> = map(deserializer::deserialize)

// TODO move
interface JsonSerde<VALUE : Any> : JsonSerializer<VALUE>, JsonDeserializer<VALUE>

// TODO move
fun interface JsonSerializer<in VALUE : Any> {

    fun serialize(value: VALUE): JSONObject
}

// TODO move
fun interface JsonDeserializer<out VALUE : Any> {

    fun deserialize(json: JSONObject): VALUE
}