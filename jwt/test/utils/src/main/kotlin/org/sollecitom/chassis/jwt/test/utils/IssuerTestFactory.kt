package org.sollecitom.chassis.jwt.test.utils

import org.jose4j.jwk.OctetKeyPairJsonWebKey
import org.sollecitom.chassis.core.domain.naming.Name
import com.element.dpg.libs.chassis.core.test.utils.stubs.random
import com.element.dpg.libs.chassis.core.utils.RandomGenerator
import org.sollecitom.chassis.jwt.domain.JwtIssuer
import org.sollecitom.chassis.jwt.jose4j.issuer.ED25519JoseIssuerAdapter
import org.sollecitom.chassis.kotlin.extensions.text.string

context(RandomGenerator)
fun newED25519JwtIssuer(keyId: String = random.string(wordLength = 6), name: Name = Name.random()): JwtIssuer {

    val keyPair = newKeyPair(OctetKeyPairJsonWebKey.SUBTYPE_ED25519)
    return ED25519JoseIssuerAdapter(keyPair, keyId, name)
}