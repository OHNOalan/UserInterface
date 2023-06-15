package ui.lectures.javafx.mvc.propertiesmvcextended.controller

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import ui.lectures.javafx.mvc.propertiesmvcextended.model.Model

class ToolBar(private val model: Model): HBox() {
    init {
        this.children.add(Button("Add Image", ImageView(Image("file:src/main/assets/addImage.png",20.0,20.0,true,true))).apply {
            onAction = EventHandler { model.addImage() }
        })
        this.children.add(Button("Del Image",ImageView(Image("file:src/main/assets/deleteImage.png",20.0,20.0,true,true))).apply {
            onAction = EventHandler { model.delImage() }
        })
        this.children.add(Button("Rotate Left",ImageView(Image("file:src/main/assets/rotateLeft.png",20.0,20.0,true,true))).apply {
            onAction = EventHandler { model.rotateLeft() }
        })
        this.children.add(Button("Rotate Right",ImageView(Image("file:src/main/assets/rotateRight.png",20.0,20.0,true,true))).apply {
            onAction = EventHandler { model.rotateRight() }
        })
        this.children.add(Button("Zoom In",ImageView(Image("file:src/main/assets/zoomIn.png",20.0,20.0,true,true))).apply {
            onAction = EventHandler { model.zoomIn() }
        })
        this.children.add(Button("Zoom Out",ImageView(Image("file:src/main/assets/zoomOut.png",20.0,20.0,true,true))).apply {
            onAction = EventHandler { model.zoomOut() }
        })
        this.children.add(Button("Reset",ImageView(Image("file:src/main/assets/reset.png",20.0,20.0,true,true))).apply {
            onAction = EventHandler { model.reset() }
        })
        this.children.add(Button("Cascade",ImageView(Image("file:src/main/assets/cascade.png",20.0,20.0,true,true))).apply {
            onAction = EventHandler { model.cascade() }
        })
        this.children.add(Button("Tile",ImageView(Image("file:src/main/assets/tile.png",20.0,20.0,true,true))).apply {
            onAction = EventHandler { model.tile() }
        })
        this.children.forEach {
            setMargin(it, Insets(5.0))
        }
    }
}