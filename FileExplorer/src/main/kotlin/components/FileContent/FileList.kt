package components.FileContent

import components.AppStatusbar
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import java.io.File


class FileList(val statusbar: AppStatusbar, val contentsDisplay: ContentDisplay, val stage: Stage?) : StackPane() {
    // determine starting directory
    // this will be the "test" subfolder in your project directory
    private val rootPath = "${System.getProperty("user.dir")}/test/"
    // dir is the path of current directory
    private var dirPath = rootPath
    private var dir = File(dirPath)
    // file is the path of current selected file/directory
    private var selectedFile = ""
    private var dirDepth = 0
    private val listView = ListView<String>()

    init {
        listView.selectionModel.selectIndices(-1)
        listView.selectionModel.selectedItemProperty().addListener { _, oldValue, newValue ->
            selectedFile = newValue?: ""
            statusbar.changePath(dirPath + selectedFile)
            contentsDisplay.select(selectedFile)
        }
        listView.onMouseClicked = EventHandler {
            if(it.clickCount == 2){
                if(File(dirPath + selectedFile).isDirectory){
                    dirDepth += 1
                    dirPath = dirPath + selectedFile + "/"
                    selectedFile = ""
                    dir = File(dirPath)
                    updateFileList()
                    listView.selectionModel.selectIndices(-1) // might change selectedFile
                }
            }
        }

        updateFileList()
        deSelect()
        this.children.add(listView)
        this.prefWidth = 200.0

        this.requestFocus()
    }

    private fun deSelect() {
        listView.selectionModel.selectIndices(-1)
        statusbar.changePath(dirPath)
        contentsDisplay.changeDirPath(dirPath)
        contentsDisplay.select("")
    }

    private fun updateFileList() {
        val fileList = dir.list()
        fileList.sort()

        listView.items.clear()
        listView.items.addAll(fileList)

        statusbar.changePath(dirPath)
        contentsDisplay.changeDirPath(dirPath)
    }

    fun Home(){
        dirDepth = 0
        dirPath = rootPath
        dir = File(dirPath)
        updateFileList()
        deSelect()
    }

    fun Prev(){
        if(dirDepth != 0) {
            dirDepth -= 1
            dir = dir.parentFile
            dirPath = dir.path + "/"
            updateFileList()
            deSelect()
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
            deSelect()
        }
    }

    fun Delete(){
        if(selectedFile != ""){
            val stage = Stage()
            val comp = VBox().apply { alignment = Pos.CENTER }
            val buttons = HBox().apply { alignment = Pos.CENTER ; padding = Insets(10.0) }
            val confirmMessage = Text("You want to delete file \"${listView.selectionModel.selectedItem}\"?")
            val cancel = Button("Cancel").apply {onMouseClicked = EventHandler {stage.close()}}
            val ok = Button("OK").apply {
                onMouseClicked = EventHandler {
                    File(dirPath + selectedFile).delete()
                    updateFileList()
                    deSelect()
                    stage.close()
                }
            }
            buttons.children.addAll(cancel,ok)
            comp.children.add(confirmMessage)
            comp.children.add(buttons)

            val stageScene = Scene(comp, 300.0, 100.0)
            stage.scene = stageScene
            stage.show()
        }
    }

    fun Move(){
        if(selectedFile != ""){
            val directoryChooser = DirectoryChooser().apply {
                initialDirectory = File(dirPath)
            }
            val selectedDirectory = directoryChooser.showDialog(stage)
            if(selectedDirectory != null){
                val newPath = selectedDirectory.absolutePath
                File(dirPath + selectedFile).renameTo(File(newPath + "/" + selectedFile))
                updateFileList()
                deSelect()
            }
        }
    }

    fun Rename(){
        if(selectedFile != ""){
            val stage = Stage()
            val comp = VBox().apply { alignment = Pos.CENTER }
            val buttons = HBox().apply { alignment = Pos.CENTER }
            val renameMessage = Text("Enter new name of the file \"${selectedFile}\"?")
            val nameField = TextField().apply {
                promptText = "Enter the name"
            }
            val warnMessage = Label("").apply {
                textProperty().addListener() { _, _, newValue ->
                    if(newValue.isEmpty().not()) comp.children.add(2,this)
                    else comp.children.remove(this)
                }
                textFill = Color.RED
            }
            val reset = Button("Reset").apply {
                onAction = EventHandler {
                    nameField.text = ""
                    warnMessage.text = ""
                }
            }
            val cancel = Button("Cancel").apply {onAction = EventHandler {stage.close()}}
            val ok = Button("OK").apply {
                onMouseClicked = EventHandler {
                    // required name detection
                    val name = nameField.text
                    if(name == "") {
                        warnMessage.text = "Name cannot be empty string."
                    } else if(name == selectedFile) {
                        warnMessage.text = "Name cannot be the same as previous."
                    } else if(name.lastIndexOf('\\') != -1) {
                        warnMessage.text = "Name cannot contain \" \\ \" char."
                    } else if(name.lastIndexOf('/') != -1) {
                        warnMessage.text = "Name cannot contain \" / \" char."
                    } else {
                        File(dirPath + selectedFile).renameTo(File(dirPath + nameField.text))
                        updateFileList()
                        deSelect()
                        stage.close()
                    }
                }
            }

            setMargin(reset, Insets(0.0,10.0,0.0,10.0))
            setMargin(cancel, Insets(0.0,10.0,0.0,10.0))
            setMargin(ok, Insets(0.0,10.0,0.0,10.0))

            buttons.children.addAll(reset,cancel,ok)
            comp.children.add(renameMessage)
            comp.children.add(nameField)
            comp.children.add(buttons)

            setMargin(nameField,Insets(10.0))

            val stageScene = Scene(comp, 300.0, 100.0)
            stage.scene = stageScene
            stage.show()
        }
    }
}