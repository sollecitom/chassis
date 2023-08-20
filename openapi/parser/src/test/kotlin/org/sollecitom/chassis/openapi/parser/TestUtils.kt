package org.sollecitom.chassis.openapi.parser

import assertk.Assert
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.sollecitom.chassis.resource.utils.ResourceLoader
import java.net.URL

fun Assert<OpenApiValidator.Result>.isValid() = given { actual -> assertThat(actual.valid).isTrue() }

fun Assert<OpenApiValidator.Result>.isInvalid() = given { actual -> assertThat(actual.valid).isFalse() }

fun readResourceWithName(resourceName: String): URL = ResourceLoader.resolve(resourceName)