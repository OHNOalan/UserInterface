package ui.lectures.javafx.mvc.propertiesmvcextended.model

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color


class ImageDisplay(private var displayIndex: DisplayIndex, url : String, model: Model) : Region() {
    private val image = ImageView(Image(url,imagePrefWidthMax,imagePrefHeightMax,true,true)).apply {
        translateX = borderWidth
        translateY = borderWidth
    }

    // preserve for dragging
    private var curTranslateX = translateX
    private var curTranslateY = translateY

    // preserve for cascade state
    private var cascadeCoordinateX = translateX
    private var cascadeCoordinateY = translateY
    private var cascadeRotate = rotate
    private var cascadeScaleX = scaleX
    private var cascadeScaleY = scaleY

    // preserve for switching mode
    private var curCoordinateX = translateX
    private var curCoordinateY = translateY
    private var startX = 0.0
    private var startY = 0.0
    private var move = false
    val fileName = url.substring(url.lastIndexOf("/") + 1, url.length)

    init {
        children.add(image)
        print("width: $width, height: $height")
//        if(displayIndex == DisplayIndex.TILE) model.tile()
        addEventFilter(MouseEvent.MOUSE_PRESSED) {
            toFront()
            model.select(this)
            if(displayIndex == DisplayIndex.CASCADE) {
                curTranslateX = translateX
                curTranslateY = translateY
                startX = it.sceneX
                startY = it.sceneY
                move = true
            }
            it.consume()
        }
        addEventFilter(MouseEvent.MOUSE_DRAGGED) {
            if(displayIndex == DisplayIndex.CASCADE) {
                if (move) {
                    translateX = curTranslateX + it.sceneX - startX
                    translateY = curTranslateY + it.sceneY - startY
                }
            }
            it.consume()
        }
        addEventFilter(MouseEvent.MOUSE_RELEASED) {
            if(displayIndex == DisplayIndex.CASCADE) {
                curCoordinateX += it.sceneX - startX
                curCoordinateY += it.sceneY - startY
                move = false
            }
            it.consume()
        }
    }

    fun select() {
        apply {
            border = BorderObject
        }
    }

    fun deselect() {
        apply {
            border = null
        }
    }

    fun operate(operation : OperationIndex) {
        when(operation) {
            OperationIndex.ROTATE_LEFT -> rotate -= 10.0
            OperationIndex.ROTATE_RIGHT -> rotate += 10.0
            OperationIndex.ZOOM_IN -> {
                scaleX += 0.25
                scaleY += 0.25
            }
            OperationIndex.ZOOM_OUT -> {
                scaleX -= 0.25
                scaleY -= 0.25
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
                translateX += cascadeCoordinateX - curCoordinateX
                translateY += cascadeCoordinateY - curCoordinateY
                curCoordinateX = cascadeCoordinateX
                curCoordinateY = cascadeCoordinateY
                rotate = cascadeRotate
                scaleX = cascadeScaleX
                scaleY = cascadeScaleY
            }
        }
        displayIndex = DisplayIndex.CASCADE
    }

    fun tile(x : Double, y : Double, scale : Double = 1.0) {
        when(displayIndex) {
            DisplayIndex.TILE -> {}
            DisplayIndex.CASCADE -> {
                cascadeCoordinateX = curCoordinateX
                cascadeCoordinateY = curCoordinateY
                cascadeRotate = rotate
                cascadeScaleX = scaleX
                cascadeScaleY = scaleY
            }
        }
        println("size (width: $width, height: $height) initial (x: $x, y: $y) ")
//        val x = x + (imagePrefWidthMax - width) / 2
//        val y = y + (imagePrefHeightMax - height) / 2
        println("final (x: $x, y: $y)")
        displayIndex = DisplayIndex.TILE
        operate(OperationIndex.RESET)
        translateX += x - curCoordinateX
        translateY += y - curCoordinateY
        curCoordinateX = x
        curCoordinateY = y
        scaleX = scale
        scaleY = scale
    }
}