package org.sollecitom.chassis.ddd.domain

interface Query : Instruction {

    override val type: Type

    companion object

    interface Type : Happening.Type {

        companion object
    }
}