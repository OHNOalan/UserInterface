package view

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Text
import HEIGHT
import WIDTH
import view.titleScene.Instruction

class EndScene(gameScore: Int, win: Boolean) : BorderPane() {
    init {
        prefWidth = WIDTH
        prefHeight = HEIGHT
        val endTitle = Text().apply {
            text = when(win){
                true -> "YOU WIN!"
                false -> "YOU LOSE!"
            }
            style = "-fx-font-weight: bold;" +
                    "-fx-font-size: 50px;"
        }
        val instr = VBox(endTitle).apply {
            padding = Insets(30.0)
            alignment = Pos.CENTER
        }
        val score = Instruction("Final Score: $gameScore")
        val start = Instruction("ENTER - Start New Game")
        val move = Instruction("I - Back to Instructions")
        val quit = Instruction("Q - Quit Game")
        val level = Instruction("1 or 2 or 3 - Start New Game at a specific level")
        val result = VBox().apply {
            prefWidth = 600.0
            prefHeight = 600.0
            opacity = 0.5
            style = "-fx-background-color: #FFFFFF;"
            children.addAll(instr,score,start,move,quit,level)
            alignment = Pos.CENTER
            padding = Insets(0.0,0.0,80.0,0.0)
        }
        center = result
    }
}