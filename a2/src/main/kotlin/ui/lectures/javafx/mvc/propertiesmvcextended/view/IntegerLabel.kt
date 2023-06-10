package ui.lectures.javafx.mvc.propertiesmvcextended.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model

/**
 * IntegerLabel displays the value of the [Model][ui.lectures.javafx.mvc.propertiesmvcextended.model.Model] on a label.
 */
class IntegerLabel(private val model: Model) : VBox(), InvalidationListener {

    private val countLabel = Label()
    private val sumLabel = Label()

    init {
        children.addAll(countLabel, sumLabel)
        spacing = 5.0
        alignment = Pos.CENTER
        model.SizeProperty.addListener(this) // listen to a Property
        model.SumProperty.addListener(this) // listen to another Property
        invalidated(null) // call to set initial content
    }

    override fun invalidated(observable: Observable?) {
        if (observable == model.SizeProperty || observable == null)
            countLabel.text = "There are currently ${model.SizeProperty.value} elements in the list." // set text of label if notified by the Property
        if (observable == model.SumProperty || observable == null)
            sumLabel.text = "The sum of all elements in the list is ${model.SumProperty.value}." // set text of label if notified by the Property
    }

}