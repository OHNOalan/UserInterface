package view.gameScene

import enemyHeight
import enemyImage
import enemyWidth
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import models.Model



class Enemies(val model: Model): Pane(), InvalidationListener{
//    private val image = ImageView(Image(enemyImage[type-1].first, 20.0,20.0,true, true))
//    private val bulletImage = ImageView(Image(enemyImage[type-1].second, 20.0, 20.0, true, true))
    init {
        model.EnemyList.addListener(this)
        invalidated(null)
    }

    override fun invalidated(observable: Observable?) {
        children.clear()
        model.EnemyList.forEach {
            val enemy = ImageView(Image(
                enemyImage[it.type.value].first,
                enemyWidth, enemyHeight,false,true))
            with(enemy) {
                xProperty().bind(it.PositionX)
                yProperty().bind(it.PositionY)
            }
            children.add(enemy)
        }
    }
}