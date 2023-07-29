package org.sollecitom.chassis.core.domain.identity.factory

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.core.domain.identity.ulid.ULIDFactory
import org.sollecitom.chassis.core.domain.identity.ulid.java.JavaUlidFactory
import kotlin.random.Random
import kotlin.random.asJavaRandom

private class UniqueIdFactoryAdapter(random: Random = Random, clock: Clock = Clock.System) : UniqueIdFactory {

    override val ulid: ULIDFactory = UlidFactoryAdapter(random, clock)

    private class UlidFactoryAdapter(random: Random, clock: Clock) : ULIDFactory {

        private val delegate = JavaUlidFactory.newMonotonicInstance(random.asJavaRandom()) {
            clock.now().toEpochMilliseconds()
        }

        override fun invoke() = delegate.create().let(::ULID)

        override fun invoke(timestamp: Instant) = delegate.create(timestamp.toEpochMilliseconds()).let(::ULID)
    }
}

operator fun UniqueIdFactory.Companion.invoke(random: Random = Random, clock: Clock = Clock.System): UniqueIdFactory = UniqueIdFactoryAdapter(random, clock)