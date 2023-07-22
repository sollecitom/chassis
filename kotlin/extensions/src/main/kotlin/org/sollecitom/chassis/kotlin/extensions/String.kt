package org.sollecitom.chassis.kotlin.extensions

fun String.replaceFrom(delimiter: String, replacement: String, missingDelimiterValue: String = this): String = when (val index = indexOf(delimiter)) {
    -1 -> missingDelimiterValue
    else -> replaceRange(index, length, replacement)
}