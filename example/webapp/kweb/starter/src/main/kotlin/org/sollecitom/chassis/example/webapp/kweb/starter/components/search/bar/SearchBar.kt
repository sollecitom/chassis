package org.sollecitom.chassis.example.webapp.kweb.starter.components.search.bar

import kweb.*
import kweb.plugins.fomanticUI.fomantic
import kweb.state.KVar
import org.sollecitom.chassis.example.webapp.kweb.starter.component.template.ComponentTemplate
import org.sollecitom.chassis.example.webapp.kweb.starter.component.template.InputElementWrapper
import org.sollecitom.chassis.example.webapp.kweb.starter.component.template.WithComponentControls
import org.sollecitom.chassis.example.webapp.kweb.starter.components.Components
import org.sollecitom.chassis.example.webapp.kweb.starter.core.aliases.Attributes
import org.sollecitom.chassis.example.webapp.kweb.starter.core.listeners.ElementListenersBuilder

interface SearchBar : ComponentTemplate, InputElementWrapper, WithComponentControls {

    interface Style {
        val div: Attributes
        val icon: Attributes
    }

    object DefaultFomaticStyle : Style {
        override val div: Attributes get() = fomantic.ui.icon.input
        override val icon: Attributes get() = fomantic.search.icon
    }

    interface Listeners {

        fun onValueChanged(handle: KVar<String>.(oldValue: String, newValue: String) -> Unit)

        fun onSearch(handle: KVar<String>.() -> Unit)

        fun raw(configure: ElementListenersBuilder<InputElement>.() -> Unit)
    }
}