package org.sollecitom.chassis.example.webapp.kweb.starter.component.template

import kweb.Element

interface ElementWrapper<ELEMENT : Element> {

    val rawValue: ELEMENT
}