package org.sollecitom.chassis.example.webapp.kweb.starter.core.listeners

import kweb.Element
import kweb.html.events.OnImmediateReceiver
import kweb.html.events.OnReceiver

interface ElementListenersBuilder<ELEMENT : Element> {

    val element: ELEMENT

    val on: OnReceiver<Element>
    val onImmediate: OnImmediateReceiver<Element>

    companion object
}