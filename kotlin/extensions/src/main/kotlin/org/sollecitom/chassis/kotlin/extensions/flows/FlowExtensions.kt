package org.sollecitom.chassis.kotlin.extensions.flows

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull

suspend inline fun <reified T> Flow<*>.firstOrNull(): T? = filterIsInstance<T>().firstOrNull()
suspend inline fun <reified T> Flow<*>.first(): T = filterIsInstance<T>().first()

suspend inline fun <reified T> Flow<*>.lastOrNull(): T? = filterIsInstance<T>().lastOrNull()
suspend inline fun <reified T> Flow<*>.last(): T = filterIsInstance<T>().last()