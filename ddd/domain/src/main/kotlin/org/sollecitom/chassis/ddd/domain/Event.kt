package org.sollecitom.chassis.ddd.domain

import org.sollecitom.chassis.core.domain.traits.Identifiable
import org.sollecitom.chassis.core.domain.traits.Timestamped

interface Event : Happening, Identifiable, Timestamped {

    override val type: Type

    companion object

    interface Type : Happening.Type {

        companion object
    }
}

