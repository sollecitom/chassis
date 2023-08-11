package org.sollecitom.chassis.example.webapp.kweb.starter.component.templating

import kweb.ElementCreator
import org.sollecitom.chassis.example.webapp.kweb.starter.core.aliases.Component

interface ComponentTemplate : Component {

    fun ElementCreator<*>.render()

    override fun invoke(html: ElementCreator<*>) = with(html) { render() }
}