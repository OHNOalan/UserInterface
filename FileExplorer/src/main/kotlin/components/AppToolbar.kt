package components

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox

class AppToolbar(appContent: AppContent) : HBox() {
    init {
        val homeImage = ImageView(Image("file:src/main/assets/home.png",15.0,15.0,true,true))
        val homeButton = Button("Home",homeImage)
        homeButton.onAction = EventHandler { appContent.Home() }

        val prevImage = ImageView(Image("file:src/main/assets/prev.png",15.0,15.0,true,true))
        val prevButton = Button("Prev",prevImage)
        prevButton.onAction = EventHandler { appContent.Prev() }

        val nextImage = ImageView(Image("file:src/main/assets/next.png",15.0,15.0,true,true))
        val nextButton = Button("Next",nextImage)
        nextButton.onAction = EventHandler { appContent.Next() }

        val deleteImage = ImageView(Image("file:src/main/assets/delete.png",15.0,15.0,true,true))
        val deleteButton = Button("Delete",deleteImage)
        deleteButton.onAction = EventHandler { appContent.Delete() }

        val moveImage = ImageView(Image("file:src/main/assets/move.png",15.0,15.0,true,true))
        val moveButton = Button("Move",moveImage)
        moveButton.onAction = EventHandler { appContent.Move() }

        val renameImage = ImageView(Image("file:src/main/assets/rename.png",15.0,15.0,true,true))
        val renameButton = Button("Rename",renameImage)
        renameButton.onAction = EventHandler { appContent.Rename() }

        this.children.addAll(listOf(homeButton,prevButton,nextButton,deleteButton,moveButton,renameButton))
        for(button in this.children){
            setMargin(button, Insets(2.0,5.0,2.0,5.0))
        }
    }
}