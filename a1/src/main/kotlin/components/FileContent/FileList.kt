package components.FileContent

import components.AppStatusbar
import javafx.scene.control.ScrollPane
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import java.io.File

class FileList(val statusbar: AppStatusbar, val contentDisplay: ContentDisplay) : ScrollPane() {
    // determine starting directory
    // this will be the "test" subfolder in your project directory
    private val rootPath = "${System.getProperty("user.dir")}/test/"
    // dir is the path of current directory
    private var dirPath = rootPath
    private var dir = File(rootPath)
    // file is the path of current selected file/directory
    private var filePath = dirPath
    private val fileList = dir.list()
    private val listContainer = VBox()
    private val toggleGroup = ToggleGroup().apply {
        selectedToggleProperty().addListener { _, _, newValue ->
            val file = "${(newValue as ToggleButton?)?.text ?: ""}"
            select(file)
        }
    }
    init {
        fileList.sort()
        fileList.forEach {
            val toggle = ToggleButton(
                it,
                Pane().apply {
                    style = "-fx-border-color: red; -fx-border-width:4; "
                }
            )
            toggleGroup.apply { toggle.toggleGroup = this }
            listContainer.children.add(toggle)
        }
        statusbar.changePath(dirPath)
        contentDisplay.changePath(dirPath)
        contentDisplay.select("")
//        toggleGroup.selectToggle(toggleGroup.toggles.first())
        this.content = listContainer
        this.prefWidth = 200.0
    }

    private fun select(file : String) {
        filePath = dirPath + file
        statusbar.changePath(filePath)
        contentDisplay.select(file)
    }
}