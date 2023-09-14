package components

import javafx.scene.layout.VBox

class AppTopbar(appContent: AppContent) : VBox() {
    init {
        this.children.add(AppMenubar(appContent))
        this.children.add(AppToolbar(appContent))
    }
}
