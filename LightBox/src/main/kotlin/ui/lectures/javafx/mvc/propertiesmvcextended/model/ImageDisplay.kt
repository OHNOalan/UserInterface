package ui.lectures.javafx.mvc.propertiesmvcextended.model

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import java.lang.Math.random


class ImageDisplay(private var displayIndex: DisplayIndex, url : String, xRange : Double, yRange: Double, model: Model)
    : ImageView(){

    // preserve for drag
    private var curCoordinateX = 0.0
    private var curCoordinateY = 0.0
    private var startX = 0.0
    private var startY = 0.0
    private var move = false

    // preserve for cascade state
    private var cascadeCoordinateX = x
    private var cascadeCoordinateY = y
    private var cascadeRotate = rotate
    private var cascadeScaleX = scaleX
    private var cascadeScaleY = scaleY

    // preserve for selected
    private val rectangle = Rectangle(x,y).apply {
        stroke = Color.AQUA
        strokeWidth = 5.0
        fill = Color.TRANSPARENT
    }

    val fileName = url.substring(url.lastIndexOf("/") + 1, url.length)

    init {
        val i = Image(url,imagePrefWidthMax,imagePrefHeightMax,true,true)
        image = i
        rectangle.xProperty().bind(xProperty())
        rectangle.yProperty().bind(yProperty())
        rectangle.widthProperty().bind(i.widthProperty())
        rectangle.heightProperty().bind(i.heightProperty())
        rectangle.rotateProperty().bind(rotateProperty())
        rectangle.scaleXProperty().bind(scaleXProperty())
        rectangle.scaleYProperty().bind(scaleYProperty())

        addEventFilter(MouseEvent.MOUSE_PRESSED) {
            model.select(this)
            if(displayIndex == DisplayIndex.CASCADE) {
                curCoordinateX = x
                curCoordinateY = y
                startX = it.sceneX
                startY = it.sceneY
                move = true
            }
            it.consume()
        }
        addEventFilter(MouseEvent.MOUSE_DRAGGED) {
            if(displayIndex == DisplayIndex.CASCADE) {
                if (move) {
                    x = curCoordinateX + it.sceneX - startX
                    y = curCoordinateY + it.sceneY - startY
                    if (x < 0) x = 0.0
                    if (y < 0) y = 0.0
                }
            }
            it.consume()
        }
        addEventFilter(MouseEvent.MOUSE_RELEASED) {
            if(displayIndex == DisplayIndex.CASCADE) {
                move = false
            }
            it.consume()
        }
        x = random() * (xRange - i.width)
        y = random() * (yRange - i.height)
    }

    fun select() {
        (parent as Pane).children.add(rectangle)
        toFront()
    }

    fun deselect() {
        (parent as Pane).children.remove(rectangle)
    }

    fun operate(operation : OperationIndex) {
        when(operation) {
            OperationIndex.ROTATE_LEFT -> rotate -= 10.0
            OperationIndex.ROTATE_RIGHT -> rotate += 10.0
            OperationIndex.ZOOM_IN -> {
                if(scaleX < 5.0) {
                    scaleX += 0.25
                    scaleY += 0.25
                }
            }
            OperationIndex.ZOOM_OUT -> {
                if(scaleX > 0.25) {
                    scaleX -= 0.25
                    scaleY -= 0.25
                }
            }
            OperationIndex.RESET -> {
                scaleX = 1.0
                scaleY = 1.0
                rotate = 0.0
            }
        }
    }

    fun cascade() {
        when(displayIndex) {
            DisplayIndex.CASCADE -> {}
            DisplayIndex.TILE -> {
                x = cascadeCoordinateX
                y = cascadeCoordinateY
                rotate = cascadeRotate
                scaleX = cascadeScaleX
                scaleY = cascadeScaleY
                displayIndex = DisplayIndex.CASCADE
            }
        }
    }

    fun tile(destinationX : Double, destinationY : Double) {
        when(displayIndex) {
            DisplayIndex.TILE -> {}
            DisplayIndex.CASCADE -> {
                cascadeCoordinateX = x
                cascadeCoordinateY = y
                cascadeRotate = rotate
                cascadeScaleX = scaleX
                cascadeScaleY = scaleY
                operate(OperationIndex.RESET)
                displayIndex = DisplayIndex.TILE
            }
        }
        x = destinationX
        y = destinationY
    }
}