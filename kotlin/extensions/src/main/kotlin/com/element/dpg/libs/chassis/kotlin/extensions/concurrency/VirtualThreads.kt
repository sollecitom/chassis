package com.element.dpg.libs.chassis.kotlin.extensions.concurrency

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

private val virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor()
private val virtualThreadDispatcher = virtualThreadExecutor.asCoroutineDispatcher()

val Dispatchers.VirtualThreads: CoroutineDispatcher get() = virtualThreadDispatcher