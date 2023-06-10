package ui.lectures.javafx.mvc.propertiesmvcextended

import javafx.application.Application
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Separator
import javafx.scene.layout.VBox
import javafx.stage.Stage
import ui.lectures.javafx.mvc.propertiesmvcextended.controller.AddButton
import ui.lectures.javafx.mvc.propertiesmvcextended.controller.IncrementButton
import ui.lectures.javafx.mvc.propertiesmvcextended.controller.ResetListButton
import ui.lectures.javafx.mvc.propertiesmvcextended.controller.ResetValueButton
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model
import ui.lectures.javafx.mvc.propertiesmvcextended.view.DoubleLabel
import ui.lectures.javafx.mvc.propertiesmvcextended.view.DoubleSlider
import ui.lectures.javafx.mvc.propertiesmvcextended.view.IntegerLabel
import ui.lectures.javafx.mvc.propertiesmvcextended.view.ListView

class HelloPropertiesMVCExtended : Application() {
    override fun start(stage: Stage) {

        val myModel = Model()

        stage.apply {
            title = "Hello CS349! - JavaFX.MVC.Properties.Extended"
            scene = Scene(VBox(
                DoubleLabel(myModel),
                DoubleSlider(myModel),
                ListView(myModel),
                IntegerLabel(myModel),
                Separator(Orientation.HORIZONTAL),
                IncrementButton(myModel),
                ResetValueButton(myModel),
                AddButton(myModel),
                ResetListButton(myModel),
            ).apply {
                alignment = Pos.CENTER
                spacing = 10.0
            }, 320.0, 320.0)
        }.show()
    }
}