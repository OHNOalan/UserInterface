package components

import javafx.scene.layout.VBox

class AppTopbar : VBox() {
    init {
        this.children.add(AppMenubar())
        this.children.add(AppToolbar())
    }
}
