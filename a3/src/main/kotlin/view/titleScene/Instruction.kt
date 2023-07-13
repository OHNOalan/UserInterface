package view.titleScene

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import instructionStyle

class Instruction(text: String) : VBox() {
    init {
        val text = Text(text).apply {
            style = instructionStyle
        }
        children.add(text)
        alignment = Pos.CENTER
        padding = Insets(10.0)
    }
}