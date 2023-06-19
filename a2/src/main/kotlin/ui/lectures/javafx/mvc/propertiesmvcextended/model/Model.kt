package ui.lectures.javafx.mvc.propertiesmvcextended.model

import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyDoubleWrapper
import javafx.beans.property.ReadOnlyIntegerProperty
import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.beans.property.ReadOnlyListProperty
import javafx.beans.property.ReadOnlyListWrapper
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.lang.Math.ceil

class Model(private val stage : Stage?) {

    val PaneWidth = ReadOnlyDoubleWrapper(0.0)
    val PaneHeight = ReadOnlyDoubleWrapper(0.0)

    private val rootPath = "${System.getProperty("user.dir")}/test/"

    private val displayState = ReadOnlyObjectWrapper<DisplayIndex>(DisplayIndex.CASCADE)
    val DisplayState: ReadOnlyObjectProperty<DisplayIndex> = displayState.readOnlyProperty

    private val imagesCount = ReadOnlyIntegerWrapper()
    val ImagesCount: ReadOnlyIntegerProperty = imagesCount.readOnlyProperty

    private val selectedImage = ReadOnlyObjectWrapper(null as ImageDisplay?)
    val SelectedImage: ReadOnlyObjectProperty<ImageDisplay?> = selectedImage.readOnlyProperty

    private val imageList = ReadOnlyListWrapper(FXCollections.observableList(List(0) { } as List<ImageDisplay> ))
    val ImageList: ReadOnlyListProperty<ImageDisplay> = imageList.readOnlyProperty

    private fun setSelectedImage(currentImage: ImageDisplay?) {
        selectedImage.value = currentImage
    }

    fun select(imageDisplay: ImageDisplay) {
        imageList.value.forEach {
            it.deselect()
        }
        imageDisplay.select()
        setSelectedImage(imageDisplay)
    }

    fun deselect() {
        imageList.value.forEach {
            it.deselect()
        }
        setSelectedImage(null)
    }

    fun addImage() {
        val fileChooser = FileChooser().apply { initialDirectory = File(rootPath) }
        val selectedFile = fileChooser.showOpenDialog(stage)
        if(selectedFile != null){
            val path = selectedFile.toURI().toURL().toExternalForm()
            val newImage = ImageDisplay(displayState.value,path,this)
            imageList.value.add(newImage)
            newImage.select()
            select(newImage)
            if(displayState.value == DisplayIndex.TILE) {
                tile()
            }
        } else {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "ERROR"
            alert.contentText = "Select File is not a picture"
            alert.showAndWait()
        }
    }

    fun delImage() {
        if(selectedImage.value != null) {
            imageList.value.remove(selectedImage.value)
            setSelectedImage(null)
            if(displayState.value == DisplayIndex.TILE) tile()
        } else {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "ERROR"
            alert.contentText = "Select File is not a picture"
            alert.showAndWait()
        }
    }

    fun operate(operation : OperationIndex) {
        selectedImage.value?.operate(operation)
    }

    fun cascade() {
        imageList.value.forEach { it.cascade() }
//        println("Cascade called")
    }

    fun tile() {
        displayState.value = DisplayIndex.TILE
//        println("tile activate")
        println("PaneWidth: ${PaneWidth.value} / imagePrefWidth: ${imagePrefWidthMax}")
        val size = imageList.value.size
        val col = (PaneWidth.value / imagePrefWidthMax).toInt()
        val row = ceil(size.toDouble() / col).toInt()
        println("col : $col, row : $row")
        PaneHeight.value = row * imagePrefHeightMax

        var x = imagePrefWidthMax
        var y = imagePrefHeightMax
        imageList.forEach {
            print("(x: $x, y: $y) (width: ${it.width}, height: ${it.height})")
            it.tile(x-imagePrefWidthMax,y-imagePrefHeightMax)
            if(x + imagePrefWidthMax <= PaneWidth.value) {
                x += imagePrefWidthMax
            } else {
                x = imagePrefWidthMax
                y += imagePrefHeightMax
            }
        }
    }

    init {
        imagesCount.bind(imageList.sizeProperty())
    }
}