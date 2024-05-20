package com.element.dpg.libs.chassis.logger.slf4j.provider

import com.element.dpg.libs.chassis.logger.slf4j.adapter.Slf4jLoggerFactory
import com.element.dpg.libs.chassis.logger.slf4j.adapter.Slf4jMDCAdapter
import org.slf4j.ILoggerFactory
import org.slf4j.IMarkerFactory
import org.slf4j.helpers.BasicMarkerFactory
import org.slf4j.spi.MDCAdapter
import org.slf4j.spi.SLF4JServiceProvider

class KLogSlf4jServiceProvider : SLF4JServiceProvider {

    private val loggerFactory: ILoggerFactory = Slf4jLoggerFactory()
    private val markerFactory: IMarkerFactory = BasicMarkerFactory()
    private val mdcAdapter: MDCAdapter = Slf4jMDCAdapter()

    override fun getLoggerFactory(): ILoggerFactory = loggerFactory

    override fun getMarkerFactory(): IMarkerFactory = markerFactory

    override fun getMDCAdapter(): MDCAdapter = mdcAdapter

    override fun getRequestedApiVersion(): String = REQUESTED_API_VERSION

    override fun initialize() {
        // No-op
    }

    companion object {
        // To avoid constant folding by the compiler, this field must *not* be final
        const val REQUESTED_API_VERSION = "2.0.6" // !final
    }
}