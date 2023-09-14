package view

import BGMCycle
import BGMRate
import backgroundSoundTrack
import explosionSoundTrack
import javafx.animation.AnimationTimer
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.scene.media.AudioClip
import javafx.stage.Stage
import models.EnemyMissile
import models.Model
import view.gameScene.Enemies
import view.gameScene.Missiles
import view.gameScene.Player
import view.gameScene.StatusBar

class GameScene(val stage: Stage, val model: Model, sceneWidth: Double, sceneHeight: Double) : StackPane(), InvalidationListener {
    val game = BorderPane()
    var gameEnd = SimpleBooleanProperty(false);
    var soundCount = 0
    var rate = BGMRate
    var cycle = 0
    private val timer: AnimationTimer = object : AnimationTimer() {
        override fun handle(now: Long) {
            if(soundCount % rate == 0) {
                AudioClip(backgroundSoundTrack[soundCount/rate]).play()
            }
            soundCount++
            if(soundCount == rate*4) {
                soundCount = 0
                cycle++
                if(cycle == BGMCycle) {
                    cycle = 0
                    if(rate > 5) rate--
                }
            }
        }
    }
    init {
        style = "-fx-background-color: #000000;"
        isFocusTraversable = true
        game.isFocusTraversable = true
        model.GameOver.addListener(this)
        with(game){
            isFocusTraversable = true
            top = StatusBar(model)
            center = StackPane(Enemies(model),Missiles(model))
            bottom = Player(model)
        }
        children.add(game)
        game.addEventHandler(KeyEvent.KEY_PRESSED) {
            when(it.code){
                KeyCode.A, KeyCode.LEFT -> model.moveLeft()
                KeyCode.D, KeyCode.RIGHT ->model.moveRight()
                KeyCode.SPACE -> model.fire()
                else -> Unit
            }
        }
        game.addEventHandler(KeyEvent.KEY_RELEASED) {
            when(it.code){
                KeyCode.A, KeyCode.LEFT -> model.stopLeft()
                KeyCode.D, KeyCode.RIGHT -> model.stopRight()
                else -> Unit
            }
        }
        timer.start()
    }

    override fun invalidated(observable: Observable?) {
        if(model.GameOver.value) {
            val endScene = EndScene(model.Score.value, model.GameResult.value).apply {
                prefWidth = 600.0
                prefHeight = 600.0
            }
            children.add(endScene)
            timer.stop()
            gameEnd.value = true
        }
    }
}