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

    init {
        val textField = TextField("Your name")

        val button = Button("Say hello") {
            val message = greetingService.greet(textField.value)
            add(Paragraph(message))
        }

        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        button.addClickShortcut(Key.ENTER)

        // Use custom CSS classes to apply styling. This is defined in styles.css.
        addClassName("centered-content")
        add(textField, button)
    }
}