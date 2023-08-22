package org.sollecitom.chassis.openapi.validation.http4k.validator.utils

import org.http4k.core.Headers
import org.http4k.core.toParametersMap

internal fun Headers.toMultiMap(): Map<String, Collection<String>> = toParametersMap().mapValues { it.value.filterNotNull() }