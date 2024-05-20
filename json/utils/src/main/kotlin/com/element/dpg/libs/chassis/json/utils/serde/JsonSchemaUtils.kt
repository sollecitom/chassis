package com.element.dpg.libs.chassis.json.utils.serde

import com.github.erosb.jsonsKema.JsonParser
import com.github.erosb.jsonsKema.SchemaClient
import com.github.erosb.jsonsKema.SchemaLoader
import com.github.erosb.jsonsKema.SchemaLoaderConfig
import org.json.JSONObject
import com.element.dpg.libs.chassis.resource.utils.ResourceLoader.openAsStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URI

fun jsonSchemaAt(location: String): Schema = openAsStream(location).use {

    val locationBytes = it.readAllBytes()
    val jsonSchema = JSONObject(String(locationBytes))
    val schemaJson = JsonParser(jsonSchema.toString()).parse()
    val schema = SchemaLoader(schemaJson, SchemaLoaderConfig(schemaClient, initialBaseURI)).load()
    Schema(schema, jsonSchema)
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