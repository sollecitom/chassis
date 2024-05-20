package com.element.dpg.libs.chassis.kotlin.extensions.bytes

import com.element.dpg.libs.chassis.kotlin.extensions.number.roundToCeil
import kotlin.math.log2

val Int.requiredBits: Int get() = log2(toDouble()).roundToCeil()