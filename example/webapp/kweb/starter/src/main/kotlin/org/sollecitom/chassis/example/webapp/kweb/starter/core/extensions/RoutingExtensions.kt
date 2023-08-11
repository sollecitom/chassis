package org.sollecitom.chassis.example.webapp.kweb.starter.core.extensions

import kweb.ElementCreator
import kweb.routing.RouteReceiver
import org.sollecitom.chassis.example.webapp.kweb.starter.component.template.ComponentTemplate
import org.sollecitom.chassis.example.webapp.kweb.starter.core.aliases.RoutedComponent
import org.sollecitom.chassis.example.webapp.kweb.starter.core.aliases.RoutingParams

fun RouteReceiver.path(template: String, component: ComponentTemplate) = path(template, component.asRoutedComponent())

private fun ComponentTemplate.asRoutedComponent(): RoutedComponent = object : RoutedComponent {

    override fun invoke(html: ElementCreator<*>, params: RoutingParams) = this@asRoutedComponent.invoke(html)
}