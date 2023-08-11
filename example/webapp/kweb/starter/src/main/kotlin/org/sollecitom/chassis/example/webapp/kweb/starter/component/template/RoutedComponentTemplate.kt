package org.sollecitom.chassis.example.webapp.kweb.starter.component.template

import kweb.ElementCreator
import kweb.state.KVar
import org.sollecitom.chassis.example.webapp.kweb.starter.core.aliases.RoutedComponent

interface RoutedComponentTemplate : RoutedComponent {

    fun ElementCreator<*>.render(params: Map<String, KVar<String>>)

    override fun invoke(html: ElementCreator<*>, params: Map<String, KVar<String>>) = with(html) { render(params) }
}