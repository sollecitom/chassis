package com.element.dpg.libs.chassis.jwt.domain

import com.element.dpg.libs.chassis.core.domain.naming.Name
import java.security.PublicKey

interface JwtParty {

    val name: Name
    val publicKey: PublicKey
}