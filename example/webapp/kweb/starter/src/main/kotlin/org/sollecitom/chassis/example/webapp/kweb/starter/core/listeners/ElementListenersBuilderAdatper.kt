package org.sollecitom.chassis.example.webapp.kweb.starter.core.listeners

import kweb.Element
import kweb.html.events.OnImmediateReceiver
import kweb.html.events.OnReceiver

private class ElementListenersBuilderAdapter<ELEMENT : Element>(override val element: ELEMENT) : ElementListenersBuilder<ELEMENT> {

    override val on: OnReceiver<Element> get() = element.on
    override val onImmediate: OnImmediateReceiver<Element> get() = element.onImmediate
}

fun <ELEMENT : Element> ElementListenersBuilder.Companion.forElement(element: ELEMENT): ElementListenersBuilder<ELEMENT> = ElementListenersBuilderAdapter(element)