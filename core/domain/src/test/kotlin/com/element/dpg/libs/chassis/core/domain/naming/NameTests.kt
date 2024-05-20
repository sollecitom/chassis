package com.element.dpg.libs.chassis.core.domain.naming

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.test.utils.specifications.EqualsAndHashCodeTestSpecification

@TestInstance(PER_CLASS)
private class NameTests {

    @Test
    fun `creating a name instance`() {

        val rawNameValue = "bruce"

        val name = Name(rawNameValue)

        assertThat(name.value).isEqualTo(rawNameValue)
    }

    @Test
    fun `attempting to create an invalid name`() {

        val invalidNameValue = "  "

        val result = runCatching { Name(invalidNameValue) }

        assertThat(result).isFailure().isInstanceOf<IllegalArgumentException>()
    }

    @Test
    fun `changing case for a name`() {

        val name = Name("bRucE")

        val uppercase = name.uppercase()
        val lowercase = name.lowercase()
        val capitalized = name.capitalized()

        assertThat(uppercase.value).isEqualTo(name.value.uppercase())
        assertThat(lowercase.value).isEqualTo(name.value.lowercase())
        assertThat(capitalized.value).isEqualTo("BRucE")
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class EqualsAndHashCode : EqualsAndHashCodeTestSpecification<Name> {

        override val sameInstance1 = Name("Bruce")
        override val sameInstance2 = Name("Bruce")
        override val anotherInstance = Name("Dick")
    }
}