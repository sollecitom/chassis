package org.sollecitom.chassis.example.webapp.vaadin.adapters.driving.web.views

import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route

@Route("")
class MainView(private val greetingService: GreetingService) : VerticalLayout() {

    private val textField = TextField("Your name")
    private val button = sayHelloButton(textField)

    init {
        // Use custom CSS classes to apply styling. This is defined in styles.css.
        addClassName("centered-content")
        add(textField, button)
    }

    private fun sayHelloButton(textField: TextField): Button {

        val button = Button("Say hello") {
            val message = greetingService.greet(textField.value)
            add(Paragraph(message))
        }

        button.addClickShortcut(Key.ENTER)
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY)

        return button
    }
}