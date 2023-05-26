package components.menuItems.actionMenu

import components.AppContent
import components.menuItems.BaseMenuItem
import javafx.scene.control.Menu
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination

class NextMenuItem(menu: Menu, val appContent: AppContent) :
    BaseMenuItem(
        menu,
        "Next",
        KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN)
    ) {
    override fun onItemClicked() {
        appContent.Next()
    }
}