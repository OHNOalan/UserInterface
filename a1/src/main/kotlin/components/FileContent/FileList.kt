package components.FileContent

import components.AppStatusbar
import javafx.event.EventHandler
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
    private var dir = File(dirPath)
    // file is the path of current selected file/directory
    private var selectedFile = ""
    private var dirDepth = 0
    private val listContainer = VBox()
    private val toggleGroup = ToggleGroup().apply {
        selectedToggleProperty().addListener { _, _, newValue ->
            val file = "${(newValue as ToggleButton?)?.text ?: ""}"
            select(file)
        }
    }
    init {
        updateFileList()
        contentDisplay.select("")
        // toggleGroup.selectToggle(toggleGroup.toggles.first())
        this.content = listContainer
        this.prefWidth = 200.0
    }

    private fun select(file : String) {
        selectedFile = file
        statusbar.changePath(dirPath + selectedFile)
        contentDisplay.select(file)
    }

    private fun updateFileList() {
        val fileList = dir.list()
        fileList.sort()

        listContainer.children.clear()
        fileList.forEach {
            val toggle = ToggleButton(
                it,
                Pane().apply {
                    style = "-fx-border-color: red; -fx-border-width:4; "
                }
            )

            if(File(dirPath + it).isDirectory){
                val fileName = it
                toggle.onMouseClicked = EventHandler {
                    if(it.clickCount == 2){
                        dirDepth += 1
                        dirPath = dirPath + fileName + "/"
                        selectedFile = ""
                        dir = File(dirPath)
                        updateFileList()
                        select("")
                    }
                }
            }

            toggleGroup.apply { toggle.toggleGroup = this }
            listContainer.children.add(toggle)
        }

        statusbar.changePath(dirPath)
        contentDisplay.changePath(dirPath)
    }

    fun Home(){
        dirPath = rootPath
        dir = File(dirPath)
        updateFileList()
        select("")
    }

    fun Prev(){
        if(dirDepth != 0) {
            dirDepth -= 1
            val oldDirName = dir.name
            dir = dir.parentFile
            dirPath = dir.path + "/"
            updateFileList()
            select(oldDirName)
        }
    }

    fun Next(){
        if(selectedFile != ""
            && File(dirPath + selectedFile).isDirectory
            && File(dirPath + selectedFile).canRead())
        {
            dirDepth += 1
            dirPath = dirPath + selectedFile + "/"
            selectedFile = ""
            dir = File(dirPath)
            updateFileList()
            select("")
        }
    }

    fun Delete(){
        if(selectedFile != ""){
            File(dirPath + selectedFile).delete()
            updateFileList()
            select("")
        }
    }

    fun Rename(){
        if(selectedFile != ""){
            val newName = "newName.png"
            File(dirPath + selectedFile).renameTo(File(dirPath + newName))
            updateFileList()
            select(newName)
        }
    }
}