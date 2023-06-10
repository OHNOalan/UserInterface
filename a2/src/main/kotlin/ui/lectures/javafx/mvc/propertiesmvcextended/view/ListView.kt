package ui.lectures.javafx.mvc.propertiesmvcextended.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.FlowPane
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model

/**
 * ListView displays the content of the list of the [Model][ui.lectures.javafx.mvc.propertiesmvcextended.model.Model].
 */
class ListView(private val model: Model) : FlowPane(Orientation.HORIZONTAL), InvalidationListener {

    init {
        alignment = Pos.CENTER
        model.MyList.addListener(this) // listen to the Property
        invalidated((null)) // call to set initial content
    }

    override fun invalidated(observable: Observable?) {
        children.clear() // removes all exiting children from the scene graph; not the most efficient approach, but good enough for CS349
        model.MyList.forEach {
            children.add(Label(it.toString()).also { // using also instead of apply, so that setMargin refers to the ListView and not the Label.
                setMargin(it, Insets(2.0))
            })
        }
    }


}