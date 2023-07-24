package org.sollecitom.chassis.http4k.jetty.utils.loom

import org.eclipse.jetty.util.thread.ThreadPool
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LoomThreadPool(val executorService: ExecutorService = Executors.newVirtualThreadPerTaskExecutor()) : ThreadPool {

    @Throws(InterruptedException::class)
    override fun join() {
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
    }

    override fun getThreads(): Int = 1

    override fun getIdleThreads(): Int = 1

    override fun isLowOnThreads(): Boolean = false

    override fun execute(command: Runnable) {
        executorService.submit(command)
    }
}