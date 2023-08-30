package org.sollecitom.chassis.json.utils

import com.github.erosb.jsonsKema.*
import org.json.JSONArray
import org.json.JSONObject
import org.sollecitom.chassis.resource.utils.ResourceLoader.openAsStream
import java.io.InputStream
import java.net.URI

//TODO write tests

fun jsonSchemaAt(location: String): Schema = openAsStream(location).use {

    val schemaJson = JsonParser(it).parse()
    val initialBaseURI = "mem://input"
    SchemaLoader(
        schemaJson = schemaJson, config = SchemaLoaderConfig(schemaClient = CustomSchemaClient(initialBaseURI), initialBaseURI = initialBaseURI)
    ).load()
//    SchemaLoader(schemaJson).load()
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