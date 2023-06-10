package ui.lectures.javafx.mvc.propertiesmvcextended.controller

import javafx.event.EventHandler
import javafx.scene.control.Button
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model

/**
 * Activating the ResetButton resets the value of the [Model][ui.lectures.javafx.mvc.propertiesmvcextended.model.Model].
 */
class ResetValueButton(private val model: Model): Button("Reset Value") {

    init {
        onAction = EventHandler {
            model.resetValue()
        }
    }
}