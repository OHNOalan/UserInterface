package ui.lectures.javafx.mvc.propertiesmvcextended.model

import javafx.beans.property.ReadOnlyIntegerProperty
import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.beans.property.ReadOnlyListProperty
import javafx.beans.property.ReadOnlyListWrapper
import javafx.collections.FXCollections
import javafx.scene.image.Image

class Model {

    // the current value of the Model
    private val imagesCount = ReadOnlyIntegerWrapper(0)

    val ImagesCount: ReadOnlyIntegerProperty = imagesCount.readOnlyProperty

    // the current list of the Model
//    private val imageList = ReadOnlyListWrapper(FXCollections.observableList(List(1) { ImageDisplay("")} ))

//    val ImageList: ReadOnlyListProperty<ImageDisplay> = imageList.readOnlyProperty

    fun addImage() {
        TODO()
    }

    fun delImage() {
        TODO()
    }

    fun rotateLeft() {
        TODO()
    }

    fun rotateRight() {
        TODO()
    }

    fun zoomIn() {
        TODO()
    }

    fun zoomOut() {
        TODO()
    }

    fun reset() {
        TODO()
    }

    fun cascade() {
        TODO()
    }

    fun tile() {
        TODO()
    }

    // a property representing the current size of the list
    private val sizeProperty = ReadOnlyIntegerWrapper()

    val SizeProperty: ReadOnlyIntegerProperty = sizeProperty.readOnlyProperty


    init {
        // Here, we are binding the properties for size and sum to myList.
        // For size, we can use the sizeProperty of myList directly.
        // For sum, we have to create a new Binding that takes the value as calculated by the function sum() on myList.
        // Another approach would be setting both properties explicitly in addToList- and resetList-functions, e.g.:
        //   sizeProperty.value = myList.sizeProperty().value
        //   sumProperty.value = myList.value.sum()
        // By using bindings in init, however, we avoid having to make these two calls in every function that mutates myList.
//        sizeProperty.bind(ImageList.sizeProperty())
    }
}