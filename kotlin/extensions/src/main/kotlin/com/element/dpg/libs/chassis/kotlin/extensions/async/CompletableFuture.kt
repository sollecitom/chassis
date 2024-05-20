package com.element.dpg.libs.chassis.kotlin.extensions.async

import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture

suspend fun CompletableFuture<Void>.await(): Unit = await().let { }