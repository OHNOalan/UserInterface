package ui.lectures.javafx.mvc.propertiesmvcextended.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.beans.binding.Bindings
import javafx.event.EventHandler
import javafx.scene.control.ScrollPane
import javafx.scene.layout.*
import ui.lectures.javafx.mvc.propertiesmvcextended.model.BorderObject
import ui.lectures.javafx.mvc.propertiesmvcextended.model.DisplayIndex
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model
class Pictures(private val model: Model) : ScrollPane(), InvalidationListener {
    private val pane = Pane().apply {
//        border = BorderObject
    }
    init {
        content = pane
        model.PaneWidth.bind(Bindings.createDoubleBinding(
            {
                if (model.DisplayState.value == DisplayIndex.TILE) {
                    width
                } else {
                    pane.width.coerceAtLeast(width)
                }
            },
            model.DisplayState,
            pane.widthProperty(),
            widthProperty()
        ))
        model.PaneHeight.bind(Bindings.max(heightProperty(), pane.heightProperty()))
        model.ImageList.addListener(this)
        invalidated(null)
        onMousePressed = EventHandler {
            println("${pane.width}, ${pane.height}, $width, $height")
            model.deselect()
        }
    }

    override fun invalidated(observable: Observable?) {
        pane.children.clear()
        model.ImageList.forEach { pane.children.add(it) }
    }
}