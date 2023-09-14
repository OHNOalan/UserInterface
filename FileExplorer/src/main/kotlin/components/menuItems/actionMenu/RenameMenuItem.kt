package components.menuItems.actionMenu

import components.AppContent
import components.menuItems.BaseMenuItem
import javafx.scene.control.Menu

class RenameMenuItem(menu: Menu, val appContent: AppContent) : BaseMenuItem(menu,"Rename") {
    override fun onItemClicked() {
        appContent.Rename()
    }
}