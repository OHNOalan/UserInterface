package components

import components.menuItems.fileMenu.ExitMenuItem
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.layout.VBox

class AppMenubar : VBox() {
    init {
        val menuBar = MenuBar()

        val fileMenu = Menu("File")
        ExitMenuItem(fileMenu)
        menuBar.menus.add(fileMenu)

        val viewMenu = Menu("View")
        menuBar.menus.add(viewMenu)

        val actionsMenu = Menu("Actions")
        menuBar.menus.add(actionsMenu)

        val optionsMenu = Menu("Options")
        menuBar.menus.add(optionsMenu)

        this.children.add(menuBar)
    }
}