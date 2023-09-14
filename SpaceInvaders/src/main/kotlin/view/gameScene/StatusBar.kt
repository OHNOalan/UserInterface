package view.gameScene

import gameTextStyle
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Insets
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Text
import models.Model
import statusBarHeight

class StatusBar(private val model: Model) : BorderPane(), InvalidationListener {
    private val scoreIndicator = Text("Score: 0").apply { style = gameTextStyle }
    private val livesIndicator = Text("Lives: 0").apply { style = gameTextStyle }
    private val levelIndicator = Text("Level: 0").apply { style = gameTextStyle }
    init {
        prefHeight = statusBarHeight
        left = HBox(scoreIndicator).apply { padding = Insets(10.0)  }
        center = HBox().apply { prefWidth = 800.0 }
        right = HBox(HBox(livesIndicator).apply { padding = Insets(10.0) },
            HBox(levelIndicator).apply { padding = Insets(10.0) })
        model.Score.addListener(this)
        model.Lives.addListener(this)
        model.Level.addListener(this)
        invalidated(null)
        padding = Insets(10.0)
//        border = Border(BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))
    }

    override fun invalidated(observable: Observable?) {
        if(observable == model.Score || observable == null) {
            scoreIndicator.text = "Score: ${model.Score.value}"
        }
        if(observable == model.Lives || observable == null) {
            livesIndicator.text = "Lives: ${model.Lives.value}"
        }
        if(observable == model.Level || observable == null) {
            levelIndicator.text = "Level: ${model.Level.value}"
        }
    }
}