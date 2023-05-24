package components

import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox

class AppToolbar : HBox() {
    init {
        val homeButton = Button("Home")
        val prevButton = Button("Prev")
        val nextButton = Button("Next")
        val deleteButton = Button("Delete")
        val renameButton = Button("Rename")

        this.children.addAll(listOf(homeButton,prevButton,nextButton,deleteButton,renameButton))
        for(button in this.children){
            setMargin(button, Insets(2.0,5.0,2.0,5.0))
        }
    }
}