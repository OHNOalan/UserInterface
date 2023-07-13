package models

import enemyHeight
import enemyWidth
import javafx.animation.AnimationTimer
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyDoubleWrapper
import missileHeight
import missileSpeed
import missileWidth
import shipHeight
import shipWidth

interface Missile {
    val model : Model
    val PositionX : ReadOnlyDoubleProperty
    val PositionY : ReadOnlyDoubleProperty
    val timer : AnimationTimer
}

class PlayerMissile(override val model: Model): Missile {
    private val positionX = ReadOnlyDoubleWrapper(model.Player.value.Position.value + shipWidth /2 - missileWidth /2)
    override val PositionX: ReadOnlyDoubleProperty = positionX.readOnlyProperty
    private var positionY = ReadOnlyDoubleWrapper(model.Player.value.PositionY.value - missileHeight)
    override val PositionY: ReadOnlyDoubleProperty = positionY.readOnlyProperty
    override val timer: AnimationTimer = object : AnimationTimer() {
        override fun handle(now: Long) {
            positionY.value -= missileSpeed
        }
    }

    fun collisionDetect(): Enemy? {
        model.EnemyList.forEach {enemy ->
            if(positionX.value <= enemy.PositionX.value + enemyWidth
                && enemy.PositionX.value <= positionX.value + missileWidth
                && positionY.value <= enemy.PositionY.value + enemyHeight
                && enemy.PositionY.value <= positionY.value + missileHeight
            ) return enemy
        }
        return null
    }
    fun stop() {
        timer.stop()
    }
    init {
        timer.start()
    }
}

class EnemyMissile(override val model: Model, enemy: Enemy): Missile {
    val type = enemy.type
    private val positionX = ReadOnlyDoubleWrapper(enemy.PositionX.value + enemyWidth /2 - missileWidth /2)
    override val PositionX: ReadOnlyDoubleProperty = positionX.readOnlyProperty
    private var positionY = ReadOnlyDoubleWrapper(enemy.PositionY.value + enemyHeight)
    override val PositionY: ReadOnlyDoubleProperty = positionY.readOnlyProperty
    override val timer: AnimationTimer = object : AnimationTimer() {
        override fun handle(now: Long) {
            positionY.value += missileSpeed
        }
    }
    fun collisionDetect(): Boolean{
        return positionX.value <= model.Player.value.Position.value + shipWidth
                && model.Player.value.Position.value <= positionX.value + enemyWidth
                && positionY.value <= model.Player.value.PositionY.value + shipHeight
                && model.Player.value.PositionY.value <= positionY.value + missileHeight
    }
    fun stop() {
        timer.stop()
    }
    init {
        timer.start()
    }
}