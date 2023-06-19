package ui.lectures.javafx.mvc.propertiesmvcextended.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.beans.binding.Bindings
import javafx.event.EventHandler
import javafx.scene.control.ScrollPane
import javafx.scene.layout.*
import javafx.scene.paint.Color
import ui.lectures.javafx.mvc.propertiesmvcextended.model.DisplayIndex
import ui.lectures.javafx.mvc.propertiesmvcextended.model.ImageDisplay
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model
import kotlin.math.max

class Pictures(private val model: Model) : ScrollPane(), InvalidationListener {
    private val pane = Pane()
    init {
        border = Border(
            BorderStroke(
                Color.rgb(200,200,200),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT
            )
        )

        model.PaneWidth.bind(pane.prefWidthProperty())
        Bindings.bindBidirectional(model.PaneHeight,pane.prefHeightProperty())

        model.PaneHeight.addListener { _,_,newValue ->
            pane.prefHeight = max(newValue.toDouble(),height)
        }
        widthProperty().addListener { _,_,newValue ->
            pane.prefWidth = newValue.toDouble()
        }
        heightProperty().addListener { _,_,newValue ->
            pane.prefHeight = max(newValue.toDouble(),model.PaneHeight.value)
        }

        vbarPolicy = ScrollBarPolicy.ALWAYS
        hbarPolicy = ScrollBarPolicy.ALWAYS

        content = pane

        model.ImageList.addListener(this)
        invalidated(null)

        onMousePressed = EventHandler {
            model.deselect()
        }
        pane.prefWidthProperty().addListener {_,_,_ ->
            if(model.DisplayState.value == DisplayIndex.TILE) model.tile()
        }
        pane.prefWidthProperty().addListener {_,_,_ ->
            if(model.DisplayState.value == DisplayIndex.TILE) model.tile()
        }
    }

    override fun invalidated(observable: Observable?) {
        pane.children.clear()
        model.ImageList.forEach {
            pane.children.add(it)
        }
    }
}