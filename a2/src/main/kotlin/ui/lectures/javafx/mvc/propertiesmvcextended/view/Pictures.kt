package ui.lectures.javafx.mvc.propertiesmvcextended.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.layout.*
import javafx.scene.paint.Color
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model

class Pictures(private val model: Model) : VBox(), InvalidationListener {
    init {
        model.ImagesCount.addListener(this)
        invalidated(null)
        border = Border(
            BorderStroke(
                Color.rgb(200,200,200),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT
            )
        )
    }

    override fun invalidated(observable: Observable?) {
//        model.ImageList.forEach {
//            children.add(ImageView(it))
//        }
    }
}