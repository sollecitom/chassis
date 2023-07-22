package org.sollecitom.chassis.json.test.utils

import assertk.Assert
import assertk.assertions.containsAll
import assertk.assertions.containsOnly
import assertk.assertions.support.expected
import assertk.assertions.support.show
import org.everit.json.schema.Schema
import org.everit.json.schema.ValidationException
import org.json.JSONObject

fun Assert<JSONObject>.containsOnly(pair: Pair<String, Any>, vararg others: Pair<String, Any>) = given { actual ->

    assertThat(actual.toMap()).containsOnly(*arrayOf(pair) + others)
}

fun Assert<JSONObject>.containsAll(pair: Pair<String, Any>, vararg others: Pair<String, Any>) = given { actual ->

    assertThat(actual.toMap()).containsAll(*arrayOf(pair) + others)
}

fun Assert<JSONObject>.compliesWith(schema: Schema) = given { actual ->

    try {
        schema.validate(actual)
    } catch (e: ValidationException) {
        expected("JSON that complies with schema at :${show(schema.location)}/${schema.title} but there were errors: ${show(e.allMessages)}")
    }
}