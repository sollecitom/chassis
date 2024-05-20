package com.element.dpg.libs.chassis.core.domain.identity.factory

import com.element.dpg.libs.chassis.core.domain.identity.SortableTimestampedUniqueIdentifier
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import java.time.Instant as JavaInstant

interface SortableTimestampedUniqueIdentifierFactory<ID : SortableTimestampedUniqueIdentifier<ID>> {

    operator fun invoke(): ID

    operator fun invoke(timestamp: Instant): ID
}

operator fun <ID : SortableTimestampedUniqueIdentifier<ID>> SortableTimestampedUniqueIdentifierFactory<ID>.invoke(instant: JavaInstant) = invoke(instant.toKotlinInstant())