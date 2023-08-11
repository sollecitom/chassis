package org.sollecitom.chassis.example.webapp.kweb.starter.components

import kweb.*
import kweb.plugins.fomanticUI.fomantic
import kweb.state.KVar
import org.sollecitom.chassis.example.webapp.kweb.starter.component.templating.ComponentTemplate
import org.sollecitom.chassis.example.webapp.kweb.starter.core.aliases.Attributes
import org.sollecitom.chassis.example.webapp.kweb.starter.core.listeners.ElementListenersBuilder
import org.sollecitom.chassis.example.webapp.kweb.starter.core.listeners.forElement
import org.sollecitom.chassis.logger.core.loggable.Loggable

class SearchBar(private val placeholder: String?, private val style: Style = DefaultFomaticStyle, private val listen: Listeners.() -> Unit = {}) : ComponentTemplate {

    private lateinit var searchBar: InputElement

    override fun ElementCreator<*>.render() {

        div(style.div) {
            searchBar = input(type = InputType.search, placeholder = placeholder)
            SearchBarListeners(searchBar).listen()
            i(style.icon)
        }
    }

    var value: String
        get() = searchBar.value.value
        set(value) {
            searchBar.value.value = value
        }

    val rawValue: InputElement get() = searchBar

    fun enable() {
        searchBar.enable()
    }

    fun disable() {
        searchBar.disable()
    }

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

    private class SearchBarListeners(private val searchBar: InputElement) : Listeners {

        override fun onValueChanged(handle: KVar<String>.(oldValue: String, newValue: String) -> Unit) {

            searchBar.value.addListener { oldValue, newValue -> searchBar.value.handle(oldValue, newValue) }
        }

        override fun onSearch(handle: KVar<String>.() -> Unit) {

            searchBar.on.search { searchBar.value.handle() }
        }

        override fun raw(configure: ElementListenersBuilder<InputElement>.() -> Unit) = ElementListenersBuilder.forElement(searchBar).configure()
    }

    companion object : Loggable()
}