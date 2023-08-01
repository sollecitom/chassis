package org.sollecitom.chassis.kotlin.extensions.text

import java.util.*
import java.util.regex.Pattern

private val whitespace by lazy { Pattern.compile("\\s") }

fun String.withoutWhitespace(): String = whitespace.matcher(this).replaceAll("")

fun String.replaceFrom(delimiter: String, replacement: String, missingDelimiterValue: String = this): String {

    val index = indexOf(delimiter)
    return if (index == -1) missingDelimiterValue else replaceRange(index, length, replacement)
}

fun String.capitalized(): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }