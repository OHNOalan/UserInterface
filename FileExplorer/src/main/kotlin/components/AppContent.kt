package components

import components.FileContent.ContentDisplay
import components.FileContent.FileList
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class AppContent(statusbar: AppStatusbar, stage: Stage?) : BorderPane() {
    private val content = ContentDisplay()
    private val fileList = FileList(statusbar,content,stage)
    init {
        this.apply {
            center = content
            left = fileList
        }
    }

    fun Home(){ fileList.Home() }
    fun Prev(){ fileList.Prev() }
    fun Next(){ fileList.Next() }
    fun Delete(){ fileList.Delete() }
    fun Move(){ fileList.Move() }
    fun Rename(){ fileList.Rename() }
}