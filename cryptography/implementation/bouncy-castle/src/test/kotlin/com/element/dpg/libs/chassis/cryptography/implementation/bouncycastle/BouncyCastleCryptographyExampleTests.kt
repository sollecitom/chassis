package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.cryptography.domain.factory.CryptographicOperations
import com.element.dpg.libs.chassis.cryptography.test.specification.CryptographyTestSpecification


@TestInstance(PER_CLASS)
private class BouncyCastleCryptographyExampleTests : CryptographyTestSpecification {

    override val cryptography: CryptographicOperations get() = CryptographicOperations.bouncyCastle()
}