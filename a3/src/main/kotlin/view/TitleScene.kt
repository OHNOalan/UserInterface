package view

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.text.Text
import logo
import titleBackgroundStyle
import view.titleScene.Instruction

class TitleScene : BorderPane() {
    init {
        style = titleBackgroundStyle
        top = VBox(ImageView(Image(logo, 500.0, 300.0, true, true))).apply {
            alignment = Pos.CENTER
            padding = Insets(30.0)
            }
        bottom = VBox(Text("Implemented by Alan Lee for CS349, University of Waterloo, S23")).apply {
            alignment = Pos.CENTER
            padding = Insets(10.0)
        }

        val instr = VBox(Text("Instructions").apply {
            style = "-fx-font-weight: bold;" +
                    "-fx-font-size: 50px;"
        }).apply {
            padding = Insets(30.0)
            alignment = Pos.CENTER
        }
        val start = Instruction("ENTER - Start Game")
        val move = Instruction("A or <, D or > - Move ship left or right")
        val fire = Instruction("SPACE - Fire!")
        val quit = Instruction("Q - Quit Game")
        val level = Instruction("1 or 2 or 3 - Start Game at a specific level")
        center = VBox().apply {
            children.addAll(instr,start,move,fire,quit,level)
            alignment = Pos.CENTER
            padding = Insets(0.0,0.0,80.0,0.0)
        }
    }
}