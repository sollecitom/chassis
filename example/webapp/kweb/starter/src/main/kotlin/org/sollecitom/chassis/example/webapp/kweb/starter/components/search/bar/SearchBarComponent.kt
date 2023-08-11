package org.sollecitom.chassis.example.webapp.kweb.starter.components.search.bar

import kweb.*
import kweb.state.KVar
import org.sollecitom.chassis.example.webapp.kweb.starter.components.Components
import org.sollecitom.chassis.example.webapp.kweb.starter.core.listeners.ElementListenersBuilder
import org.sollecitom.chassis.example.webapp.kweb.starter.core.listeners.forElement
import org.sollecitom.chassis.logger.core.loggable.Loggable

fun Components.searchBar(placeholder: String?, style: SearchBar.Style = SearchBar.DefaultFomaticStyle, listen: SearchBar.Listeners.() -> Unit = {}): SearchBar = SearchBarComponent(placeholder, style, listen)

private class SearchBarComponent(private val placeholder: String?, private val style: SearchBar.Style = SearchBar.DefaultFomaticStyle, private val listen: SearchBar.Listeners.() -> Unit = {}) : SearchBar {

    private lateinit var searchBar: InputElement

    override fun ElementCreator<*>.render() {

        div(style.div) {
            searchBar = input(type = InputType.search, placeholder = placeholder)
            SearchBarListeners(searchBar).listen()
            i(style.icon)
        }
    }

    override var value: String
        get() = searchBar.value.value
        set(value) {
            searchBar.value.value = value
        }

    override val rawValue: InputElement get() = searchBar

    override fun enable() {
        searchBar.enable()
    }

    override fun disable() {
        searchBar.disable()
    }

    private class SearchBarListeners(private val searchBar: InputElement) : SearchBar.Listeners {

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