package org.sollecitom.chassis.example.webapp.kweb.starter.fomantic.extensions

import kweb.classes
import kweb.plugins.fomanticUI.FomanticUIClasses

val FomanticUIClasses.home: FomanticUIClasses
    get() {
        return named("home")
    }

val FomanticUIClasses.layout: FomanticUIClasses
    get() {
        return named("layout")
    }

fun FomanticUIClasses.named(name: String): FomanticUIClasses {

    classes(name)
    return this
}