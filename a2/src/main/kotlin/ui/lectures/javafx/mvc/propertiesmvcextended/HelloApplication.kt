package ui.lectures.javafx.mvc.propertiesmvcextended

import javafx.application.Application
import javafx.beans.binding.Bindings
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Separator
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.stage.Stage
import ui.lectures.javafx.mvc.propertiesmvcextended.controller.ToolBar
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model
import ui.lectures.javafx.mvc.propertiesmvcextended.model.imagePrefHeightMax
import ui.lectures.javafx.mvc.propertiesmvcextended.view.Pictures
import ui.lectures.javafx.mvc.propertiesmvcextended.view.StatusBar

class HelloPropertiesMVCExtended : Application() {
    override fun start(stage: Stage) {

//        val root = Pane()
//
//        val image1 = ImageView(Image("file:test/picture1.png",200.0,160.0,true,true))
//        val image2 = ImageView(Image("file:test/picture2.png",200.0,160.0,true,true))
//        val save2 = image2.transforms
//        image2.apply {
//            scaleX += 0.5
//            scaleY += 0.5
//            translateX += 500.0
//            rotate = 45.0
//        }
//        root.children.addAll(image1,image2)
//
//        image1.toFront()
//        image2.toFront()
//
////        image2.apply {
////            scaleX = 1.0
////            scaleY = 1.0
////            translateX = 0.0
////            translateY = 0.0
////            rotate = 0.0
////        }
//
//        stage.apply {
//            title = "LightBox (c) 2023 Alan Lee"
//            scene = Scene(root, 1200.0, 600.0)
//        }.show()

        val myModel = Model(stage)

        val toolBar = ToolBar(myModel)
        val pictures = Pictures(myModel)
        val statusBar = StatusBar(myModel)


        val root = VBox(toolBar,pictures,statusBar)
        val primaryScene = Scene(root, 1200.0, 600.0)

        VBox.setVgrow(pictures, Priority.ALWAYS)
        root.prefHeightProperty().bind(primaryScene.heightProperty())

        stage.apply {
            title = "LightBox (c) 2023 Alan Lee"
            scene = primaryScene
            minWidth = 900.0
            minHeight = 2 * imagePrefHeightMax
        }.show()
    }
}