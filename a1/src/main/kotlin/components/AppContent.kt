package components

import components.FileContent.ContentDisplay
import components.FileContent.FileList
import javafx.scene.layout.BorderPane

class AppContent(statusbar: AppStatusbar) : BorderPane() {
    private val content = ContentDisplay()
    private val fileList = FileList(statusbar,content)
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
    fun Rename(){ fileList.Rename() }
}