package org.sollecitom.chassis.example.webapp.kweb.starter.fomantic.extensions

import kweb.classes
import kweb.plugins.fomanticUI.FomanticUIClasses

val FomanticUIClasses.home: FomanticUIClasses
    get() {
        classes("home")
        return this
    }

val FomanticUIClasses.layout: FomanticUIClasses
    get() {
        classes("layout")
        return this
    }