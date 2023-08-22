package org.sollecitom.chassis.openapi.parser

import io.swagger.v3.oas.models.media.Schema

// TODO do we still need this?
object OpenApi {

    fun enableVersion310OrHigher() {
        // TODO remove this when they'll realise this should be on by default
        // Without this parameters OpenApi v3.10+ doesn't work, as header parameters of type String end up incorrectly validated like if they were supposed to be JSON objects
        System.setProperty(Schema.BIND_TYPE_AND_TYPES, true.toString())
    }
}