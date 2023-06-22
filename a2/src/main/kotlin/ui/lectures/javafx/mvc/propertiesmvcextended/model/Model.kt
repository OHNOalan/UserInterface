package ui.lectures.javafx.mvc.propertiesmvcextended.model

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
import java.util.Random

class Model(private val stage : Stage?) {

    private var displayState = DisplayIndex.CASCADE

    val PaneWidth = ReadOnlyDoubleWrapper(0.0)
    val PaneHeight = ReadOnlyDoubleWrapper(0.0)

    private val rootPath = "${System.getProperty("user.dir")}/test/"

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
        selectedImage.value?.deselect()
        imageDisplay.select()
        setSelectedImage(imageDisplay)
    }

    fun deselect() {
        selectedImage.value?.deselect()
        setSelectedImage(null)
    }

    fun addImage() {
        val fileChooser = FileChooser().apply { initialDirectory = File(rootPath) }
        val selectedFile = fileChooser.showOpenDialog(stage)
        if(selectedFile != null){
            val path = selectedFile.toURI().toURL().toExternalForm()
            val newImage = ImageDisplay(displayState,path,this).apply {
                x = Random().nextDouble() * (PaneWidth.value)
                y = Random().nextDouble() * (PaneHeight.value)
            }
            imageList.value.add(newImage)
            select(newImage)
            if(displayState == DisplayIndex.TILE) tile()
        } else {
            val alert = Alert(Alert.AlertType.ERROR).apply {
                title = "ERROR"
                contentText = "Select File is not a picture"
            }
            alert.showAndWait()
        }
    }

    fun delImage() {
        if(selectedImage.value != null) {
            imageList.value.remove(selectedImage.value)
            setSelectedImage(null)
            if(displayState == DisplayIndex.TILE) tile()
        } else {
            val alert = Alert(Alert.AlertType.ERROR).apply {
                title = "ERROR"
                contentText = "Select File is not a picture"
            }
            alert.showAndWait()
        }
    }

    fun operate(operation : OperationIndex) { selectedImage.value?.operate(operation) }

    fun cascade() {
        if (displayState == DisplayIndex.TILE) {
            imageList.value.forEach { it.cascade() }
        }
        displayState = DisplayIndex.CASCADE
    }

    fun tile() {
        var x = imagePrefWidthMax
        var y = imagePrefHeightMax
        imageList.forEach {
            it.tile(x-imagePrefWidthMax,y-imagePrefHeightMax)
            if(x + imagePrefWidthMax <= PaneWidth.value) {
                x += imagePrefWidthMax
            } else {
                x = imagePrefWidthMax
                y += imagePrefHeightMax
            }
        }
        displayState = DisplayIndex.TILE
    }

    init {
        imagesCount.bind(imageList.sizeProperty())
    }
}