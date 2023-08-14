package org.sollecitom.chassis.example.webapp.vaadin.adapters.driving.web.views

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.Autocapitalize
import com.vaadin.flow.component.textfield.EmailField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Validator
import com.vaadin.flow.router.Route
import org.sollecitom.chassis.logger.core.loggable.Loggable


@Route("")
class MainView(private val greetingService: GreetingService) : VerticalLayout() {

    private val newUserForm = NewUserForm { _, user ->

        logger.info { "Saved user $user" }
    }
//    private val textField = textField()
//    private val button = sayHelloButton(textField)

    init {
        addClassName("centered-content") // Use custom CSS classes to apply styling. This is defined in styles.css.
        add(newUserForm)
//        add(newUserForm, textField, button)
    }

    private fun textField() = TextField("Your name")

    private fun sayHelloButton(textField: TextField): Button {

        val button = Button("Say hello") {
            val message = greetingService.greet(textField.value)
            add(Paragraph(message))
        }

        button.addClickShortcut(Key.ENTER)
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY)

        return button
    }

    companion object : Loggable()
}

data class User(val firstName: String, val lastName: String, val email: String)

class NewUserForm(private val onSave: (ClickEvent<Button>, User) -> Unit = { _, _ -> }) : FormLayout() {

    private val firstName: TextField = TextField("First name").apply {
        isRequired = true
        minLength = 1
        maxLength = 300
        isInvalid = true
        addClientValidatedEventListener {
            save.isEnabled = isValid()
        }
    }

    private val lastName: TextField = TextField("Last name").apply {
        isRequired = true
        minLength = 1
        maxLength = 300
        isInvalid = true
        addClientValidatedEventListener {
            save.isEnabled = isValid()
        }
    }

    private val email: EmailField = EmailField("Email address").apply {
        isRequired = true
        minLength = 1
        maxLength = 1000
        isInvalid = true
        addClientValidatedEventListener {
            save.isEnabled = isValid()
        }
    }

    private val save = Button("Save") {
        onSave(it, User(firstName.value, lastName.value, email.value))
    }.apply {
        isEnabled = false
    }
    private val close = Button("Cancel") {
        clear()
    }

    init {
        add(firstName, lastName, email, buttonsLayout())
    }

    fun isValid() = !firstName.isInvalid && !lastName.isInvalid && !email.isInvalid

    fun clear() {
        firstName.clear()
        lastName.clear()
        email.clear()
    }

    private fun buttonsLayout(): HorizontalLayout {

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY)

        save.addClickShortcut(Key.ENTER)
        close.addClickShortcut(Key.ESCAPE)
        return HorizontalLayout(save, close)
    }
}