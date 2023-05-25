package components

import components.menuItems.actionMenu.*
import components.menuItems.fileMenu.ExitMenuItem
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.layout.VBox

class AppMenubar(appContent: AppContent) : VBox() {
    init {
        val menuBar = MenuBar()

        val fileMenu = Menu("File")
        ExitMenuItem(fileMenu)
        menuBar.menus.add(fileMenu)

        val viewMenu = Menu("View")
        menuBar.menus.add(viewMenu)

        val actionsMenu = Menu("Actions")
        HomeMenuItem(actionsMenu, appContent)
        PrevMenuItem(actionsMenu, appContent)
        NextMenuItem(actionsMenu, appContent)
        DeleteMenuItem(actionsMenu, appContent)
        RenameMenuItem(actionsMenu, appContent)
        menuBar.menus.add(actionsMenu)

        val optionsMenu = Menu("Options")
        menuBar.menus.add(optionsMenu)

        this.children.add(menuBar)
    }
}