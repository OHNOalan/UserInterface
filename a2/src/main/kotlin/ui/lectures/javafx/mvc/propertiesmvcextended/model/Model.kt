package ui.lectures.javafx.mvc.propertiesmvcextended.model

import javafx.beans.binding.Bindings
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyDoubleWrapper
import javafx.beans.property.ReadOnlyIntegerProperty
import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.beans.property.ReadOnlyListProperty
import javafx.beans.property.ReadOnlyListWrapper
import javafx.collections.FXCollections
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Model represents the "Model" in "Model-View-Controller".
 */
class Model {

    /**
     * The minimum value of the Model
     */
    val minValue = 0.0

    /**
     * The maximum value of the Model
     */
    val maxValue = 10.0

    // the current value of the Model
    private val myValue = ReadOnlyDoubleWrapper(minValue)

    /**
     * the current value of the [Model], exposed as a read-only Property
     */
    val MyValue: ReadOnlyDoubleProperty = myValue.readOnlyProperty

    /**
     * Sets the [value][MyValue] and notifies all listeners.
     */
    fun setValue(newValue: Double) {
        myValue.value = newValue.coerceIn(minValue, maxValue)
    }

    /**
     * Increments the [value][MyValue] by one and notifies all listeners.
     */
    fun incrementValue() {
        setValue(myValue.value + 1.0)
    }

    /**
     * Resets the [value][MyValue] and notifies all listeners.
     */
    fun resetValue() {
        setValue(minValue)
    }

    // the current list of the Model
    private val myList = ReadOnlyListWrapper(FXCollections.observableList(List(1) { 0.0 } ))

    /**
     * A list of all added [values][MyValue].
     */
    val MyList: ReadOnlyListProperty<Double> = myList.readOnlyProperty

    /**
     * Adds the [value][MyValue], rounded to two decimals, to the [list][MyList] and notifies all listeners.
     */
    fun addToList() {
        myList.value.add(BigDecimal(myValue.value).setScale(2, RoundingMode.HALF_EVEN).toDouble())
    }

    /**
     * Resets the [list][MyList] and notifies all added listeners.
     */
    fun resetList() {
        myList.clear()
    }

    // a property representing the current size of the list
    private val sizeProperty = ReadOnlyIntegerWrapper()

    /**
     * The current size of the [list][MyList].
     */
    val SizeProperty: ReadOnlyIntegerProperty = sizeProperty.readOnlyProperty

    // a property representing the sum of all elements of the list
    private val sumProperty = ReadOnlyDoubleWrapper()

    /**
     * The sum of all elements of the [list][MyList].
     */
    val SumProperty: ReadOnlyDoubleProperty = sumProperty.readOnlyProperty

    init {
        // Here, we are binding the properties for size and sum to myList.
        // For size, we can use the sizeProperty of myList directly.
        // For sum, we have to create a new Binding that takes the value as calculated by the function sum() on myList.
        // Another approach would be setting both properties explicitly in addToList- and resetList-functions, e.g.:
        //   sizeProperty.value = myList.sizeProperty().value
        //   sumProperty.value = myList.value.sum()
        // By using bindings in init, however, we avoid having to make these two calls in every function that mutates myList.
        sizeProperty.bind(myList.sizeProperty())
        sumProperty.bind(Bindings.createDoubleBinding({ myList.value.sum() }, myList))
    }
}