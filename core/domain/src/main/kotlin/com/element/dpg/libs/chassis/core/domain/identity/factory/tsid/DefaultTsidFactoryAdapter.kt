package com.element.dpg.libs.chassis.core.domain.identity.factory.tsid

import com.element.dpg.libs.chassis.core.domain.identity.TSID
import com.element.dpg.libs.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlin.random.Random
import kotlin.random.asJavaRandom
import io.hypersistence.tsid.TSID as Tsid

internal class DefaultTsidFactoryAdapter(random: Random, clock: Clock) : SortableTimestampedUniqueIdentifierFactory<TSID> {

    private val delegate = Tsid.Factory.builder().withRandom(random.asJavaRandom()).withClock(clock.now()::toJavaInstant).build()

    override fun invoke() = delegate.generate().let(::TSID)

    override fun invoke(timestamp: Instant) = delegate.generate(timestamp.toEpochMilliseconds()).let(::TSID)
}