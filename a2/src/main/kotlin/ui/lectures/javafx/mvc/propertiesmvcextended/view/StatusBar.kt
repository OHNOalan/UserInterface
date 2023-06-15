package ui.lectures.javafx.mvc.propertiesmvcextended.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.text.Text
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model

class StatusBar(private val model: Model) : Text(), InvalidationListener {
    private val count = 0;
    init {
        model.ImagesCount.addListener(this)
        invalidated(null)
    }

    override fun invalidated(observable: Observable?) {
        text = "${model.ImagesCount.value} images loaded"
    }
}