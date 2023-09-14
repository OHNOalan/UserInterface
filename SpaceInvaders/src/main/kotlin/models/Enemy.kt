package models

import EnemyIndex
import enemyAccelerateSpeed
import enemyHeight
import enemyInitSpeed
import enemyLevelIncSpeed
import enemySpacing
import enemyWidth
import javafx.animation.AnimationTimer
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyDoubleWrapper
import shipWidth

enum class MOVEMENT{
    LEFT,
    RIGHT,
    NONE
}

class Enemy(val model : Model, row : Int, col : Int, val type : EnemyIndex, level : Int,
            val sceneWidth : Double, val sceneHeight : Double) {
    private var moveSpeed = enemyInitSpeed + (level-1) * enemyLevelIncSpeed
    private var positionX = ReadOnlyDoubleWrapper((enemyWidth + enemySpacing) * col)
    val PositionX: ReadOnlyDoubleProperty = positionX.readOnlyProperty
    private var positionY = ReadOnlyDoubleWrapper((enemyHeight + enemySpacing) * row)
    val PositionY: ReadOnlyDoubleProperty = positionY.readOnlyProperty
    var movement = MOVEMENT.LEFT
    private val timer: AnimationTimer = object : AnimationTimer() {
        override fun handle(now: Long) {
            when(movement) {
                MOVEMENT.LEFT -> positionX.value -= moveSpeed
                MOVEMENT.RIGHT -> positionX.value += moveSpeed
                MOVEMENT.NONE -> Unit
            }
        }
    }
    init {
        timer.start()
    }
    fun overlap() : Boolean {
        return positionX.value <= model.Player.value.Position.value + shipWidth
                && model.Player.value.Position.value <= positionX.value + enemyWidth
                && PositionY.value + enemyHeight > model.Player.value.PositionY.value
    }
    fun moveToBoundary() : Boolean {
        return positionX.value < 0.0 || positionX.value + enemyWidth > sceneWidth
    }
    fun changeDirection() {
        when(movement) {
            MOVEMENT.LEFT -> {
                movement = MOVEMENT.RIGHT
                positionX.value += moveSpeed
            }
            MOVEMENT.RIGHT -> {
                movement = MOVEMENT.LEFT
                positionX.value -= moveSpeed
            }
            MOVEMENT.NONE -> MOVEMENT.NONE
        }
        down()
    }
    private fun down() {
        positionY.value += enemyHeight
        if(positionY.value >= sceneHeight) model.EnemyList.value.remove(this)
    }
    fun accelerate() {
        moveSpeed += enemyAccelerateSpeed
    }
    fun stop() {
        movement = MOVEMENT.NONE
        timer.stop()
    }
}