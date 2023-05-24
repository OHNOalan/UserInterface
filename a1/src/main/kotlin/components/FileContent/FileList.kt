package components.FileContent

import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import java.io.File

class FileList : ScrollPane() {
    init {
        // determine starting directory
        // this will be the "test" subfolder in your project directory
        val dir = File("${System.getProperty("user.dir")}/test/")
        println(dir.absolutePath)
        val fileList = dir.list()
        val box = VBox()
        fileList.forEach {
            val text = Text(it).apply {
//                background = "#AAAAAA"
            }
            box.children.add(Text(it))
            box.children.add(Text(it))
        }
        this.content = box
        this.prefWidth = 200.0
    }
}