package components.menuItems.actionMenu

import components.AppContent
import components.menuItems.BaseMenuItem
import javafx.scene.control.Menu

class HomeMenuItem(menu: Menu, val appContent: AppContent) : BaseMenuItem(menu,"Home") {
    override fun onItemClicked() {
        appContent.Home()
    }
}