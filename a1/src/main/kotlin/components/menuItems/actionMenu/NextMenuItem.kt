package components.menuItems.actionMenu

import components.AppContent
import components.menuItems.BaseMenuItem
import javafx.scene.control.Menu

class NextMenuItem(menu: Menu, val appContent: AppContent) :
    BaseMenuItem(
        menu,
        "Next") {
    override fun onItemClicked() {
        appContent.Next()
    }
}