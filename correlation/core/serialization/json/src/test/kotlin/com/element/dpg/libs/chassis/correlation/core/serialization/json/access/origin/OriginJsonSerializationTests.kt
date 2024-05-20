package com.element.dpg.libs.chassis.correlation.core.serialization.json.access.origin

import com.element.dpg.libs.chassis.core.domain.networking.IpAddress
import com.element.dpg.libs.chassis.core.test.utils.testProvider
import com.element.dpg.libs.chassis.core.utils.CoreDataGenerator
import com.element.dpg.libs.chassis.correlation.core.domain.access.origin.Origin
import com.element.dpg.libs.chassis.correlation.core.test.utils.access.origin.create
import com.element.dpg.libs.chassis.json.test.utils.JsonSerdeTestSpecification
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class OriginJsonSerializationTests : JsonSerdeTestSpecification<Origin>, CoreDataGenerator by CoreDataGenerator.testProvider {

    override val jsonSerde get() = Origin.jsonSerde

    override fun parameterizedArguments() = listOf(
        "with-V4-ip-address" to Origin.create(ipAddress = IpAddress.create("127.0.0.1")),
        "with-V6-ip-address" to Origin.create(ipAddress = IpAddress.create("::1"))
    )
}