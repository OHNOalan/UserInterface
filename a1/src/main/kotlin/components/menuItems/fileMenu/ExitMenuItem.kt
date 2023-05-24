package components.menuItems.fileMenu

import components.menuItems.BaseMenuItem
import javafx.scene.control.Menu
import kotlin.system.exitProcess

class ExitMenuItem(menu: Menu) : BaseMenuItem(menu,"Exit") {
    override fun onItemClicked() {
        exitProcess(0)
    }
}