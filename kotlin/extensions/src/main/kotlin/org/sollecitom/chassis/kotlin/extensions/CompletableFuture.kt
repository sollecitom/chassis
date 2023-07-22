package org.sollecitom.chassis.kotlin.extensions

import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture

suspend fun CompletableFuture<Void>.await(): Unit = await().let {  }