package models

import enemyHeight
import javafx.animation.AnimationTimer
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyDoubleWrapper
import shipHeight
import shipMoveSpeed
import shipWidth
import statusBarHeight
import kotlin.random.Random

class Ship (val sceneWidth: Double, sceneHeight: Double) {
    private var position = ReadOnlyDoubleWrapper(sceneWidth/2 - shipWidth /2)
    val Position: ReadOnlyDoubleProperty = position.readOnlyProperty
    private val positionY = ReadOnlyDoubleWrapper(sceneHeight - shipHeight - statusBarHeight - 1.5 * enemyHeight)
    val PositionY: ReadOnlyDoubleProperty = positionY.readOnlyProperty
    var left = false
    var right = false
    private val timer: AnimationTimer = object : AnimationTimer() {
        override fun handle(now: Long) {
            if(left && position.value >= shipMoveSpeed) position.value -= shipMoveSpeed;
            if(right && position.value + shipWidth + shipMoveSpeed <= sceneWidth) position.value += shipMoveSpeed
        }
    }
    init {
        timer.start()
    }
    fun reset() {
        position.value = Random.nextDouble(0.0,sceneWidth - shipWidth)
    }
    fun moveLeft () {
        right = false
        left = true
    }
    fun moveRight () {
        left = false
        right = true
    }
    fun stopLeft () {
        left = false
    }
    fun stopRight () {
        right = false
    }
    fun stop() {
        timer.stop()
    }
}