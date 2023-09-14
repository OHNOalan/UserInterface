package components.menuItems.actionMenu

import components.AppContent
import components.menuItems.BaseMenuItem
import javafx.scene.control.Menu

class DeleteMenuItem(menu: Menu, val appContent: AppContent) : BaseMenuItem(menu,"Delete") {
    override fun onItemClicked() {
        appContent.Delete()
    }
}