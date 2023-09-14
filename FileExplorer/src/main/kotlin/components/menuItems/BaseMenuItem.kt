package components.menuItems

import javafx.event.EventHandler
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.input.KeyCombination

abstract class BaseMenuItem(
    menu: Menu,
    itemName: String,
    hotkey: KeyCombination? = null
){
    init {
        val menuItem = MenuItem(itemName)
        menu.items.add(menuItem)
        menuItem.onAction = EventHandler {
            onItemClicked()
        }

        hotkey?.let { menuItem.accelerator = hotkey }
    }

    abstract fun onItemClicked()
}