package org.sollecitom.chassis.configuration.utils

import org.http4k.cloudnative.env.Environment
import org.http4k.lens.BiDiLens

fun Environment.Companion.from(vararg entries: Pair<BiDiLens<Environment, *>, String>) = from(*entries.map { it.first.meta.name to it.second }.toTypedArray())