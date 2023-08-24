package org.sollecitom.chassis.core.domain.identity.factory

import com.github.f4b6a3.ulid.UlidFactory
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.sollecitom.chassis.core.domain.identity.StringId
import org.sollecitom.chassis.core.domain.identity.ulid.ULID
import org.sollecitom.chassis.core.domain.identity.ulid.ULIDFactory
import kotlin.random.Random
import kotlin.random.asJavaRandom

private class UniqueIdFactoryAdapter(random: Random = Random, clock: Clock = Clock.System) : UniqueIdFactory {

    override val ulid: ULIDFactory = UlidFactoryAdapter(random, clock)
    override val string: UniqueIdentifierFactory<StringId> = StringFactoryAdapter(random) { ulid().stringValue }

    private class UlidFactoryAdapter(random: Random, clock: Clock) : ULIDFactory {

        private val delegate = UlidFactory.newMonotonicInstance(random.asJavaRandom()) {
            clock.now().toEpochMilliseconds()
        }

        override fun invoke() = delegate.create().let(::ULID)

        override fun invoke(timestamp: Instant) = delegate.create(timestamp.toEpochMilliseconds()).let(::ULID)
    }

    private class StringFactoryAdapter(private val random: Random, private val string: Random.() -> String) : UniqueIdentifierFactory<StringId> {

        override fun invoke() = invoke(random.string())

        override fun invoke(value: String) = StringId(value)
    }
}

operator fun UniqueIdFactory.Companion.invoke(random: Random = Random, clock: Clock = Clock.System): UniqueIdFactory = UniqueIdFactoryAdapter(random, clock)