package org.sollecitom.chassis.core.domain.identity.factory.tsid

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import org.sollecitom.chassis.core.domain.identity.TSID
import org.sollecitom.chassis.core.domain.identity.factory.SortableTimestampedUniqueIdentifierFactory
import kotlin.random.Random
import kotlin.random.asJavaRandom

internal class NodeSpecificTsidFactoryAdapter(nodeId: Int, nodeBits: Int, random: Random, clock: Clock) : SortableTimestampedUniqueIdentifierFactory<TSID> {

    private val delegate = io.hypersistence.tsid.TSID.Factory.builder().withNode(nodeId).withNodeBits(nodeBits).withRandom(random.asJavaRandom()).withClock(clock.now()::toJavaInstant).build()

    override fun invoke() = delegate.generate().let(::TSID)

    override fun invoke(timestamp: Instant) = delegate.generate(timestamp.toEpochMilliseconds()).let(::TSID)
}