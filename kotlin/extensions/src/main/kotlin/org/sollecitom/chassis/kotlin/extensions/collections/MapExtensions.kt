package org.sollecitom.chassis.kotlin.extensions.collections

fun Map<String, String?>.withKeysPrefix(prefix: String, separator: String = "."): Map<String, String?> = mapKeys { (key, _) -> "$prefix$separator$key" }