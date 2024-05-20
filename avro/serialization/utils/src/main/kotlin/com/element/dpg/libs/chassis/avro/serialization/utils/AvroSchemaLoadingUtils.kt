package com.element.dpg.libs.chassis.avro.serialization.utils

import com.element.dpg.libs.chassis.resource.utils.ResourceLoader
import org.apache.avro.Schema

fun avroSchemaAt(relativePath: String, referencedTypes: Map<String, Schema> = emptyMap()): Schema = Schema.Parser().addTypes(referencedTypes).parse(ResourceLoader.readAsText(relativePath))