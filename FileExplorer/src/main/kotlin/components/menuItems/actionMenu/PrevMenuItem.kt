package components.menuItems.actionMenu

import components.AppContent
import components.menuItems.BaseMenuItem
import javafx.scene.control.Menu
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination

class PrevMenuItem(menu: Menu, val appContent: AppContent) :
    BaseMenuItem(
        menu,
        "Prev",
        KeyCodeCombination(KeyCode.BACK_SPACE)
    ) {
    override fun onItemClicked() {
        appContent.Prev()
    }
}