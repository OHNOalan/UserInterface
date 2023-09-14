package components

import javafx.scene.layout.HBox
import javafx.scene.text.Text
import java.awt.Insets

class AppStatusbar() : HBox() {
    private var path : String = ""
    private var text = Text(path)
    init {
        this.children.add(text)
    }

    fun changePath(newPath : String) {
        path = newPath
        text.text = path
    }
}