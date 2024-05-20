package com.element.dpg.libs.chassis.core.test.utils

import assertk.Assert
import assertk.assertions.isEqualTo
import com.element.dpg.libs.chassis.core.domain.naming.Name

fun Assert<Name>.hasValue(expectedValue: String) = given { actual ->

    assertThat(actual.value).isEqualTo(expectedValue)
}