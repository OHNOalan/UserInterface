package ui.lectures.javafx.mvc.propertiesmvcextended.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.event.EventHandler
import javafx.scene.control.ScrollPane
import javafx.scene.layout.*
import ui.lectures.javafx.mvc.propertiesmvcextended.model.BorderObject
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model
class Pictures(private val model: Model) : ScrollPane(), InvalidationListener {
    private val pane = Pane().apply {
        border = BorderObject
    }
    init {
        content = pane
        pane.minWidthProperty().bind(widthProperty())
        pane.minHeightProperty().bind(heightProperty())
        model.PaneWidth.bind(pane.widthProperty())
        model.PaneHeight.bind(pane.heightProperty())
        model.ImageList.addListener(this)
        invalidated(null)
        onMousePressed = EventHandler {model.deselect()}
    }

    override fun invalidated(observable: Observable?) {
        pane.children.clear()
        model.ImageList.forEach { pane.children.add(it) }
    }
}