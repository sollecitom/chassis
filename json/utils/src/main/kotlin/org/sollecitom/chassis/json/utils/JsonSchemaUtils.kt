package org.sollecitom.chassis.json.utils

import com.github.erosb.jsonsKema.*
import org.json.JSONArray
import org.json.JSONObject
import org.sollecitom.chassis.resource.utils.ResourceLoader.openAsStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URI

fun jsonSchemaAt(location: String): Schema = openAsStream(location).use {

    val schemaJson = JsonParser(it).parse()
    SchemaLoader(schemaJson, SchemaLoaderConfig(schemaClient, initialBaseURI)).load()
}

private const val initialBaseURI = "mem://input"
private val schemaClient: SchemaClient by lazy { CachedSchemaClient(CustomSchemaClient(initialBaseURI)) }

private class CachedSchemaClient(private val delegate: SchemaClient) : SchemaClient by delegate {

    private val cache: MutableMap<URI, ByteArray> = mutableMapOf()

    override fun get(uri: URI): InputStream = ByteArrayInputStream(
        cache.computeIfAbsent(uri) {
            val out = ByteArrayOutputStream()
            delegate.get(it).transferTo(out)
            out.toByteArray()
        }
    )
}

private class CustomSchemaClient(private val initialBaseURI: String) : SchemaClient {

    override fun get(uri: URI): InputStream {
        val resourceName = uri.toString().removePrefix(initialBaseURI).removePrefix("/")
        return openAsStream(resourceName)
    }
}

fun Schema.validate(json: IJsonValue) = Validator.forSchema(this).validate(json)

fun Schema.validate(json: JSONObject) = validate(JsonParser(json.toString()).parse())

fun Schema.validate(json: JSONArray) = validate(JsonParser(json.toString()).parse())