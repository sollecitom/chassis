package com.element.dpg.libs.chassis.web.api.utils.negotiation

import org.http4k.format.auto
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.ContentNegotiation

fun <T> ContentNegotiation.Companion.auto(lenses: List<BiDiBodyLens<T>>) = auto(lenses.first(), *lenses.drop(1).toTypedArray())