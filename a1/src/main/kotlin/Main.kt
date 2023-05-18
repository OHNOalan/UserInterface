import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage

class Main : Application() {
    override fun start(stage: Stage) {

        val leftPane = Pane().apply {
            prefWidth = 100.0
            background = Background(BackgroundFill(Color.valueOf("#ff00ff"), null, null))
        }

        val centrePane = Pane().apply {
            prefWidth = 100.0
            background = Background(BackgroundFill(Color.valueOf("#0000ff"), null, null))
        }

        val topPane = Pane().apply {
            prefWidth = 100.0
            background = Background(BackgroundFill(Color.valueOf("#00ffff"), null, null))
        }

        val root = BorderPane().apply {
            left = leftPane
            center = centrePane
            top = topPane
        }

        with (stage) {
            scene = Scene(root, 600.0, 400.0)
            title = "A1"
            show()
        }
    }
}