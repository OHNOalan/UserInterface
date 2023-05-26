package components.menuItems.actionMenu

import components.AppContent
import components.menuItems.BaseMenuItem
import javafx.scene.control.Menu

class MoveMenuItem(menu: Menu, val appContent: AppContent) : BaseMenuItem(menu,"Move") {
    override fun onItemClicked() {
        appContent.Move()
    }
}