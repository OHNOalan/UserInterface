import components.AppContent
import components.AppStatusbar
import components.AppTopbar
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class Main : Application() {
    override fun start(primaryStage: Stage?) {

        val statusbar = AppStatusbar()
        val appContent = AppContent(statusbar,primaryStage)
        val topbar = AppTopbar(appContent)

//        primaryStage?.addEventFilter(KeyEvent.KEY_PRESSED) { event ->
//            if (event.code == KeyCode.LEFT || event.code == KeyCode.RIGHT) {
//                event.consume()
//                println("left event")
//                topbar.fireEvent(event)
//            }
//        }

        // put the panels side-by-side in a container
        val root = BorderPane().apply {
            top = topbar
            bottom = statusbar
            center = appContent
        }

        // create the scene and show the stage
        with (primaryStage!!) {
            scene = Scene(root, 600.0, 400.0).apply {
                minWidth = 600.0
                minHeight = 400.0
                maxWidth = 600.0
                maxHeight = 400.0
            }
            title = "A1"
            show()
        }
    }
}