package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider

internal const val BC_PROVIDER = BouncyCastleProvider.PROVIDER_NAME
internal val BCPQC_PROVIDER = BouncyCastlePQCProvider.PROVIDER_NAME