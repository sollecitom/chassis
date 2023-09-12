package org.sollecitom.chassis.avro.serialization.utils

import org.apache.avro.Schema
import org.sollecitom.chassis.resource.utils.ResourceLoader

fun avroSchemaAt(relativePath: String, referencedTypes: Map<String, Schema> = emptyMap()): Schema = Schema.Parser().addTypes(referencedTypes).parse(ResourceLoader.readAsText(relativePath))