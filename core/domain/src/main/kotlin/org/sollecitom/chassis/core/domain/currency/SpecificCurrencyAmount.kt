package org.sollecitom.chassis.core.domain.currency

interface SpecificCurrencyAmount<SELF : SpecificCurrencyAmount<SELF>> : CurrencyAmount<SELF> {

    operator fun plus(other: SELF): SELF

    operator fun minus(other: SELF): SELF
}