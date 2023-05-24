package components

import components.FileContent.ContentDisplay
import components.FileContent.FileList
import javafx.scene.layout.BorderPane

class AppContent : BorderPane() {
    init {
        this.apply {
            left = FileList()
            center = ContentDisplay()
        }
    }
}