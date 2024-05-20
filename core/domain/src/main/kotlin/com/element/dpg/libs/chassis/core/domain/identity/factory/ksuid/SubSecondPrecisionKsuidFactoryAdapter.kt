package com.element.dpg.libs.chassis.core.domain.identity.factory.ksuid

import com.element.dpg.libs.chassis.core.domain.identity.KSUID
import com.element.dpg.libs.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import com.github.f4b6a3.ksuid.KsuidFactory
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlin.random.Random
import kotlin.random.asJavaRandom

internal class SubSecondPrecisionKsuidFactoryAdapter(random: Random, clock: Clock) : SortableTimestampedUniqueIdentifierFactory<KSUID> {

    private val delegate: KsuidFactory = KsuidFactory.newSubsecondInstance(random.asJavaRandom(), clock.now()::toJavaInstant)

    override fun invoke() = delegate.create().let(::KSUID)

    override fun invoke(timestamp: Instant) = delegate.create(timestamp.toJavaInstant()).let(::KSUID)
}