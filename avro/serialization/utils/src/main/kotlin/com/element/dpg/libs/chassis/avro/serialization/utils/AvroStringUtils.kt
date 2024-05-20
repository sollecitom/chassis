package com.element.dpg.libs.chassis.avro.serialization.utils

import org.apache.avro.util.Utf8

fun Any.asString(): String = if (this is Utf8) toString() else this as String