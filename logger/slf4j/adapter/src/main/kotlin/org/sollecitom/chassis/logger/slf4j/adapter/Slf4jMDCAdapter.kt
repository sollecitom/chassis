package org.sollecitom.chassis.logger.slf4j.adapter

import org.slf4j.helpers.ThreadLocalMapOfStacks
import org.slf4j.spi.MDCAdapter
import java.util.*
import kotlin.concurrent.getOrSet

class Slf4jMDCAdapter : MDCAdapter {

    private val maps: InheritableThreadLocal<MutableMap<String, String>> = object : InheritableThreadLocal<MutableMap<String, String>>() {

        override fun childValue(parentValue: MutableMap<String, String>?) = parentValue?.let { HashMap(it) }
    }

    private val threadLocalMapOfDequeues = ThreadLocalMapOfStacks()

    private val map: MutableMap<String, String> get() = maps.getOrSet(::mutableMapOf)

    override fun put(key: String, value: String) {
        map[key] = value
    }

    override fun get(key: String): String? = map[key]

    override fun remove(key: String) {
        map.remove(key)
    }

    override fun clear() {
        map.clear()
    }

    override fun getCopyOfContextMap(): Map<String, String> = map.toMap()

    // nothing we can do about this, restricted by SLF4J API
    override fun setContextMap(map: Map<String, String>) {
        this.map.clear()
        this.map.putAll(map)
    }

    override fun pushByKey(key: String, value: String?) {
        threadLocalMapOfDequeues.pushByKey(key, value)
    }

    override fun popByKey(key: String): String = threadLocalMapOfDequeues.popByKey(key)

    override fun getCopyOfDequeByKey(key: String): Deque<String> = threadLocalMapOfDequeues.getCopyOfDequeByKey(key)

    override fun clearDequeByKey(key: String) = threadLocalMapOfDequeues.clearDequeByKey(key)
}
