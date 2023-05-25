package components

import components.FileContent.ContentDisplay
import components.FileContent.FileList
import javafx.scene.layout.BorderPane

class AppContent(statusbar: AppStatusbar) : BorderPane() {
    init {
        val content = ContentDisplay()
        val fileList = FileList(statusbar,content)
        this.apply {
            center = content
            left = fileList
        }
    }
}