package org.sollecitom.chassis.messaging.test.utils

import assertk.Assert
import assertk.assertions.isEqualTo
import org.sollecitom.chassis.messaging.domain.Message

fun <VALUE> Assert<Message<VALUE>>.matches(expected: Message<VALUE>) = given { actual ->

    assertThat(actual.key).isEqualTo(expected.key)
    assertThat(actual.value).isEqualTo(expected.value)
    assertThat(actual.properties).isEqualTo(expected.properties)
    assertThat(actual.context).isEqualTo(expected.context)
}