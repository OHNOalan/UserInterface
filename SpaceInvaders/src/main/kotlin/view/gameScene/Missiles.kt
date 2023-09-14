package view.gameScene

import enemyImage
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import missileHeight
import missileWidth
import models.Model
import playerImage

class Missiles(val model: Model) : Pane(), InvalidationListener {
    init {
        model.EnemyMissiles.addListener(this)
        model.PlayerMissiles.addListener(this)
        invalidated(null)
    }
    override fun invalidated(observable: Observable?) {
        children.clear()
        model.EnemyMissiles.value.forEach {
            val missile = ImageView(Image(
                enemyImage[it.type.value].second,
                missileWidth, missileHeight,false,true))
            with(missile) {
                xProperty().bind(it.PositionX)
                yProperty().bind(it.PositionY)
            }
            children.add(missile)
        }
        model.PlayerMissiles.value.forEach {
            val missile = ImageView(Image(
                playerImage.second,
                missileWidth, missileHeight,false,true))
            with(missile) {
                xProperty().bind(it.PositionX)
                yProperty().bind(it.PositionY)
            }
            children.add(missile)
        }
    }
}