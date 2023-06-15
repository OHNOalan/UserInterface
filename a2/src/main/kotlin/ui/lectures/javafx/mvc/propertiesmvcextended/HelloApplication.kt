package ui.lectures.javafx.mvc.propertiesmvcextended

import javafx.application.Application
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Separator
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import ui.lectures.javafx.mvc.propertiesmvcextended.controller.ToolBar
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model
import ui.lectures.javafx.mvc.propertiesmvcextended.view.Pictures
import ui.lectures.javafx.mvc.propertiesmvcextended.view.StatusBar

class HelloPropertiesMVCExtended : Application() {
    override fun start(stage: Stage) {

        val myModel = Model()

        stage.apply {
            title = "LightBox (c) 2023 Alan Lee"
            scene = Scene(BorderPane().apply {
                top = ToolBar(myModel)
                center = Pictures(myModel)
                bottom = StatusBar(myModel)
//                alignment = Pos.CENTER
//                spacing = 10.0
            }, 1200.0, 600.0)
        }.show()
    }
}