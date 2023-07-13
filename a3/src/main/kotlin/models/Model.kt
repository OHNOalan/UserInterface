package models

import ENEMY1ROW
import ENEMY2ROW
import ENEMY3ROW
import ENEMYCOL
import EnemyIndex
import EnemyScore
import enemyMissileRate
import explosionSoundTrack
import javafx.animation.AnimationTimer
import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.scene.media.AudioClip
import playerMissileRate
import shootSoundTrack


class Model(
    private val sceneWidth: Double,
    private val sceneHeight: Double,
    gameLevel: Int
) {
    private var playerMissileReady = playerMissileRate

    private var level = ReadOnlyIntegerWrapper(gameLevel)
    val Level: ReadOnlyIntegerProperty = level.readOnlyProperty

    private var score = ReadOnlyIntegerWrapper(0)
    val Score: ReadOnlyIntegerProperty = score.readOnlyProperty

    private var lives = ReadOnlyIntegerWrapper(3)
    val Lives: ReadOnlyIntegerProperty = lives.readOnlyProperty
    
    private val enemyList = ReadOnlyListWrapper(FXCollections.observableList(List(0) { } as List<Enemy> ))
    val EnemyList: ReadOnlyListProperty<Enemy> = enemyList.readOnlyProperty

    private val enemyMissiles = ReadOnlyListWrapper(FXCollections.observableList(List(0) { } as List<EnemyMissile> ))
    val EnemyMissiles = enemyMissiles.readOnlyProperty

    private var player = ReadOnlyObjectWrapper<Ship>(Ship(sceneWidth,sceneHeight))
    val Player = player.readOnlyProperty

    private val playerMissiles = ReadOnlyListWrapper(FXCollections.observableList(List(0) { } as List<PlayerMissile> ))
    val PlayerMissiles = playerMissiles.readOnlyProperty

    private var gameOver = ReadOnlyBooleanWrapper(false)
    val GameOver = gameOver.readOnlyProperty

    private var gameResult = ReadOnlyBooleanWrapper(false)
    val GameResult = gameResult.readOnlyProperty

    private var shootingCounter = 0

    private val timer: AnimationTimer = object : AnimationTimer() {
        override fun handle(now: Long) {
            changeDirection()
            checkResult()
            missileCollision()
            if(shootingCounter == enemyMissileRate) {
                shootingCounter = 0
                enemyMissiles.add(EnemyMissile(this@Model,enemyList.random()))
            }
            shootingCounter++
            if(playerMissileReady < playerMissileRate) playerMissileReady++
        }
    }
    private fun changeDirection() {
        var change = false
        enemyList.forEach {
            change = change || it.moveToBoundary()
        }
        if(change){
            enemyList.forEach {
                it.changeDirection()
            }
        }
    }
    private fun missileCollision(){
        enemyMissiles.value.forEach {
            if(it.collisionDetect()) {
                AudioClip(explosionSoundTrack).play()
                enemyMissiles.value.remove(it)
                lives.value--
                if(lives.value == 0) lose()
            }
        }
        playerMissiles.value.forEach {
            val enemy = it.collisionDetect()
            if(enemy != null) {
                AudioClip(explosionSoundTrack).play()
                playerMissiles.value.remove(it)
                enemyList.value.remove(enemy)
                score.value += EnemyScore(enemy.type)
            }
        }
    }
    private fun initEnemy() {
        enemyList.value.clear()
        for(row in 0 until ENEMY3ROW) {
            for(col in 0 until ENEMYCOL) {
                enemyList.value.add(Enemy(this,row,col, EnemyIndex.Enemy3,1,sceneWidth,sceneHeight))
            }
        }
        for(row in 0 until ENEMY2ROW) {
            for(col in 0 until ENEMYCOL) {
                enemyList.value.add(Enemy(this, ENEMY3ROW + row,col, EnemyIndex.Enemy2,1,sceneWidth,sceneHeight))
            }
        }
        for(row in 0 until ENEMY1ROW) {
            for(col in 0 until ENEMYCOL) {
                enemyList.value.add(Enemy(this,
                    ENEMY3ROW + ENEMY2ROW + row,col,
                    EnemyIndex.Enemy1,1,sceneWidth,sceneHeight))
            }
        }
    }

    /* Player movement */
    fun moveLeft() {
        player.value.moveLeft()
    }
    fun moveRight() {
        player.value.moveRight()
    }
    fun stopLeft() {
        player.value.stopLeft()
    }
    fun stopRight() {
        player.value.stopRight()
    }
    fun fire() {
        if(!gameOver.value) {
            if(playerMissileReady == playerMissileRate) {
                playerMissileReady = 0
                playerMissiles.add(PlayerMissile(this))
                AudioClip(shootSoundTrack).play()
            }
        }
    }
    private fun checkResult() {
        var lose = false
        if(enemyList.size == 0) win()
        enemyList.forEach { if (it.overlap()) lose = true }
        if(lose) {
            AudioClip(explosionSoundTrack).play()
            lose()
        }
    }
    private fun lose(){
        gameResult.value = false
        end()
    }
    private fun win(){
        gameResult.value = true
        end()
    }
    private fun end(){
        timer.stop()
        enemyList.forEach { it.stop() }
        enemyMissiles.forEach { it.stop() }
        playerMissiles.forEach { it.stop() }
        player.value.stop()
        gameOver.value = true
    }
    init {
        initEnemy()
        timer.start()
    }
}