import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.Stage
import models.Model
import view.GameScene
import view.TitleScene
import kotlin.system.exitProcess

class Main : Application() {
    override fun start(stage: Stage) {
        val inGame = false
        val gameModel : Model? = null
        var gameScene : Scene? = null

        val instructionScene = Scene(TitleScene(), WIDTH, HEIGHT)
        with(instructionScene){
            addEventHandler(KeyEvent.KEY_PRESSED) {
                when(it.code) {
                    KeyCode.ENTER -> startGame(stage,instructionScene,1)
                    KeyCode.DIGIT1 -> startGame(stage,instructionScene,1)
                    KeyCode.DIGIT2 -> startGame(stage,instructionScene,2)
                    KeyCode.DIGIT3 -> startGame(stage,instructionScene,3)
                    KeyCode.Q -> exitProcess(0)
                    else -> Unit
                }
            }
        }

        stage.apply {
            title = "Space Invaders"
            scene = instructionScene
            width = WIDTH
            height = HEIGHT
            isResizable = false
        }.show()
    }
    private fun startGame(stage: Stage, instructionScene: Scene, level: Int) {
        val gameModel = Model(WIDTH, HEIGHT,level)
        val gameScene = GameScene(stage,gameModel, WIDTH, HEIGHT)
        val scene = Scene(gameScene, WIDTH, HEIGHT)
        with(gameScene){
            gameEnd.addListener {_,_,_ ->
                if(gameEnd.value){
                    addEventHandler(KeyEvent.KEY_PRESSED) {
                        when(it.code) {
                            KeyCode.ENTER -> startGame(stage,instructionScene,1)
                            KeyCode.DIGIT1 -> startGame(stage,instructionScene,1)
                            KeyCode.DIGIT2 -> startGame(stage,instructionScene,2)
                            KeyCode.DIGIT3 -> startGame(stage,instructionScene,3)
                            KeyCode.I -> stage.scene = instructionScene
                            KeyCode.Q -> exitProcess(0)
                            else -> Unit
                        }
                    }
                }
            }
        }
        stage.scene = scene
    }
}