package com.element.dpg.libs.chassis.correlation.core.test.utils.access.origin

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.networking.IpAddress
import com.element.dpg.libs.chassis.core.test.utils.stubs.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.correlation.core.domain.access.origin.Origin

@TestInstance(PER_CLASS)
private class OriginTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `the origin generated has the V4 localhost as IP address`() {

        val origin = Origin.create()

        assertThat(origin.ipAddress).isEqualTo(IpAddress.V4.localhost)
    }

    @Test
    fun `the origin has the passed IP V4 address`() {

        val ipAddress = IpAddress.create("152.38.16.4")

        val origin = Origin.create(ipAddress)

        assertThat(origin.ipAddress).isEqualTo(ipAddress)
    }

    @Test
    fun `the origin has the passed IP V6 address`() {

        val ipAddress = IpAddress.create("2001:db8:3333:4444:CCCC:DDDD:EEEE:FFFF")

        val origin = Origin.create(ipAddress)

        assertThat(origin.ipAddress).isEqualTo(ipAddress)
    }
}