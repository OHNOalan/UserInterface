package ui.lectures.javafx.mvc.propertiesmvcextended.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.control.Label
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model

/**
 * DoubleLabel displays the value of the [Model][ui.lectures.javafx.mvc.propertiesmvcextended.model.Model] on a label.
 */
class DoubleLabel(private val model: Model) : Label(), InvalidationListener {

    init {
        model.MyValue.addListener(this) // listen to the Property
        invalidated(null) // call to set initial text
    }

    override fun invalidated(observable: Observable?) {
        text = "The current value is: ${model.MyValue.value}" // set text of label if notified by the Property
    }

}