package org.sollecitom.chassis.json.test.utils

import assertk.Assert
import assertk.assertions.containsAtLeast
import assertk.assertions.containsOnly
import assertk.assertions.support.expected
import assertk.assertions.support.show
import org.json.JSONObject
import org.sollecitom.chassis.json.utils.Schema
import org.sollecitom.chassis.test.utils.assertions.containsSameEntriesAs

fun Assert<JSONObject>.containsOnly(pair: Pair<String, Any>, vararg others: Pair<String, Any>) = given { actual ->

    assertThat(actual.toMap()).containsOnly(*arrayOf(pair) + others)
}

fun Assert<JSONObject>.containsAtLeast(pair: Pair<String, Any>, vararg others: Pair<String, Any>) = given { actual ->

    assertThat(actual.toMap()).containsAtLeast(*arrayOf(pair) + others)
}

fun Assert<JSONObject>.isEqualTo(expected: JSONObject) = given { actual ->

    assertThat(actual.toMap()).containsSameEntriesAs(expected.toMap())
}

fun Assert<JSONObject>.compliesWith(schema: Schema) = given { actual ->

    val failure = schema.validate(actual)
    if (failure != null) {
        expected("JSON that complies with schema at :${show(schema.location)} but there were errors: ${show(failure.message)}")
    }
}

fun Assert<JSONObject>.doesNotComplyWith(schema: Schema) = given { actual ->

    schema.validate(actual) ?: expected("JSON that does not comply with schema at :${show(schema.location)} but there were no errors")
}