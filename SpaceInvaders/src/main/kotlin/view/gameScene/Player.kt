package view.gameScene

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Pane
import models.Model
import playerImage
import shipHeight
import shipWidth

class Player (val model: Model) : Pane(){
    private val ship = ImageView(Image(playerImage.first, shipWidth, shipHeight,true,true))
    init {
        prefHeight = shipHeight
        isFocusTraversable = true
        ship.xProperty().bind(model.Player.value.Position)
        children.add(ship)
        addEventHandler(KeyEvent.KEY_PRESSED) {
            when(it.code){
                KeyCode.A, KeyCode.LEFT -> model.moveLeft()
                KeyCode.D, KeyCode.RIGHT ->model.moveRight()
                KeyCode.SPACE -> model.fire()
                else -> Unit
            }
        }
        addEventHandler(KeyEvent.KEY_RELEASED) {
            when(it.code){
                KeyCode.A, KeyCode.LEFT -> model.stopLeft()
                KeyCode.D, KeyCode.RIGHT -> model.stopRight()
                else -> Unit
            }
        }
    }
}