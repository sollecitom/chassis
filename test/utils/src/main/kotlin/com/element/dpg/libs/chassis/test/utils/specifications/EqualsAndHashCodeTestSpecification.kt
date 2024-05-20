package com.element.dpg.libs.chassis.test.utils.specifications

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.junit.jupiter.api.Test

@Suppress("FunctionName")
interface EqualsAndHashCodeTestSpecification<CANDIDATE : Any> {

    val sameInstance1: CANDIDATE
    val sameInstance2: CANDIDATE
    val anotherInstance: CANDIDATE

    @Test
    fun `equals contract is observed`() {

        check(sameInstance1 !== sameInstance2) { "`sameInstance1` and `sameInstance2` must be equal but cannot be the same instance" }

        assertThat(sameInstance1).isEqualTo(sameInstance1)
        assertThat(sameInstance2).isEqualTo(sameInstance2)
        assertThat(anotherInstance).isEqualTo(anotherInstance)

        assertThat(sameInstance1).isEqualTo(sameInstance2)
        assertThat(sameInstance2).isEqualTo(sameInstance1)

        assertThat(sameInstance1).isNotEqualTo(anotherInstance)
        assertThat(anotherInstance).isNotEqualTo(sameInstance1)

        assertThat(sameInstance2).isNotEqualTo(anotherInstance)
        assertThat(anotherInstance).isNotEqualTo(sameInstance2)
    }

    @Test
    fun `hashcode contract is observed`() {

        check(sameInstance1 !== sameInstance2) { "`sameInstance1` and `sameInstance2` must be equal but cannot be the same instance" }

        assertThat(sameInstance1.hashCode()).isEqualTo(sameInstance1.hashCode())
        assertThat(sameInstance2.hashCode()).isEqualTo(sameInstance2.hashCode())
        assertThat(anotherInstance.hashCode()).isEqualTo(anotherInstance.hashCode())

        assertThat(sameInstance1.hashCode()).isEqualTo(sameInstance2.hashCode())
    }
}