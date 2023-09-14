package ui.lectures.javafx.mvc.propertiesmvcextended.view

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model

class StatusBar(private val model: Model) : BorderPane(), InvalidationListener {
    private val selectedImageText = Text("")
    private val imagesCountText = Text("0 images loaded")
    init {
        left = selectedImageText
        right = imagesCountText
        model.ImagesCount.addListener(this)
        model.SelectedImage.addListener(this)
        invalidated(null)
        padding = Insets(5.0)
    }

    override fun invalidated(observable: Observable?) {
        if(observable == model.SelectedImage || observable == null) {
            selectedImageText.text = "selected picture: ${model.SelectedImage.value?.fileName?:"Nothing Selected"}"
        }
        if(observable == model.ImagesCount || observable == null) {
            imagesCountText.text = "${model.ImagesCount.value} images loaded"
        }
    }
}