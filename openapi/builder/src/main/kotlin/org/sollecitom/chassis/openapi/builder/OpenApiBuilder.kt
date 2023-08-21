package org.sollecitom.chassis.openapi.builder

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.SpecVersion
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses

fun buildOpenApi(version: SpecVersion = SpecVersion.V31, customize: OpenApiBuilder.() -> Unit): OpenAPI = OpenApiBuilder(version).apply(customize).build()

class OpenApiBuilder(version: SpecVersion) {

    private val api = OpenAPI(version)

    fun version(version: OpenApiVersion) = version(version.value)

    fun version(version: String) {
        api.openapi(version)
    }

    fun info(customize: Info.() -> Unit = {}) {
        if (api.info == null) {
            api.info(Info())
        }
        api.info.customize()
    }

    fun path(name: String, customize: PathItem.() -> Unit = {}) {

        api.path(name, PathItem().apply(customize))
    }

    internal fun build(): OpenAPI = api

    enum class OpenApiVersion(val value: String) {
        V3_1_0("3.1.0"), V3_0_1("3.0.1")
    }
}

fun PathItem.get(builder: Operation.() -> Unit = {}) = operation(method = PathItem.HttpMethod.GET, builder)
fun PathItem.put(builder: Operation.() -> Unit = {}) = operation(method = PathItem.HttpMethod.PUT, builder)
fun PathItem.post(builder: Operation.() -> Unit = {}) = operation(method = PathItem.HttpMethod.POST, builder)
fun PathItem.delete(builder: Operation.() -> Unit = {}) = operation(method = PathItem.HttpMethod.DELETE, builder)
fun PathItem.patch(builder: Operation.() -> Unit = {}) = operation(method = PathItem.HttpMethod.PATCH, builder)
fun PathItem.head(builder: Operation.() -> Unit = {}) = operation(method = PathItem.HttpMethod.HEAD, builder)
fun PathItem.options(builder: Operation.() -> Unit = {}) = operation(method = PathItem.HttpMethod.OPTIONS, builder)
fun PathItem.trace(builder: Operation.() -> Unit = {}) = operation(method = PathItem.HttpMethod.TRACE, builder)

fun PathItem.operation(method: PathItem.HttpMethod, customize: Operation.() -> Unit = {}) = operation(method, Operation().apply(customize))

// TODO rework this to remove these builders

fun Operation.parameters(customize: ParametersBuilder.() -> Unit) {

    parameters = ParametersBuilder().apply(customize).build()
}

fun Operation.requestBody(customize: RequestBody.() -> Unit = {}) {

    requestBody = RequestBody().apply(customize)
}

fun Operation.responses(customize: ResponsesBuilder.() -> Unit) {

    responses = ResponsesBuilder().apply(customize).build()
}

fun RequestBody.content(customize: Content.() -> Unit) {

    content = Content().apply(customize)
}

fun ApiResponse.content(customize: Content.() -> Unit) {

    content = Content().apply(customize)
}

class ParametersBuilder {

    private val parameters = mutableListOf<Parameter>()

    fun add(customize: Parameter.() -> Unit) {

        parameters += Parameter().apply(customize)
    }

    fun build(): List<Parameter> = parameters
}

fun Content.mediaTypes(customize: MediaTypesBuilder.() -> Unit = {}) {

    MediaTypesBuilder().apply(customize).build().forEach { (name, mediaType) -> addMediaType(name, mediaType) }
}

class ResponsesBuilder {

    private val responses = ApiResponses()

    // TODO add a variant with HttpStatus (maybe)
    fun status(status: Int, customize: ApiResponse.() -> Unit = {}) {

        responses[status.toString()] = ApiResponse().apply(customize)
    }

    internal fun build(): ApiResponses = responses
}

class MediaTypesBuilder {

    private val mediaTypes = mutableListOf<Pair<String, MediaType>>()

    fun add(name: String, customize: MediaType.() -> Unit = {}) {

        mediaTypes.add(name to MediaType().apply(customize))
    }

    internal fun build(): List<Pair<String, MediaType>> = mediaTypes
}