package components.menuItems.fileMenu

import components.menuItems.BaseMenuItem
import javafx.scene.control.Menu
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import kotlin.system.exitProcess

class ExitMenuItem(menu: Menu) : BaseMenuItem(
    menu,
    "Exit",
    KeyCodeCombination(KeyCode.ESCAPE, KeyCombination.CONTROL_DOWN)) {
    override fun onItemClicked() {
        exitProcess(0)
    }
}