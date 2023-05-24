import components.AppContent
import components.AppTopbar
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class Main : Application() {
    override fun start(primaryStage: Stage?) {

        // create panels
//        val leftPane = Pane().apply {
//            prefWidth = 100.0
//            background = Background(BackgroundFill(Color.valueOf("#ff00ff"), null, null))
//            setOnMouseClicked { println("left pane clicked") }
//        }
//
//        val topPane = Pane().apply {
//            prefHeight = 30.0
//            background = Background(BackgroundFill(Color.valueOf("#00ff00"), null, null))
//            setOnMouseClicked { println("top pane clicked") }
//        }
//
//        val centrePane = Pane().apply {
//            prefWidth = 100.0
//            background = Background(BackgroundFill(Color.valueOf("#0000ff"), null, null))
//            setOnMouseClicked { println("centre pane clicked") }
//        }


        // put the panels side-by-side in a container
        val root = BorderPane().apply {
            center = AppContent()
            top = AppTopbar()
        }

        // create the scene and show the stage
        with (primaryStage!!) {
            scene = Scene(root, 600.0, 400.0)
            title = "A1"
            show()
        }
    }
}