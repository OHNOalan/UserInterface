//package controller
//
//import javafx.event.EventHandler
//import javafx.geometry.Insets
//import javafx.geometry.Pos
//import javafx.scene.control.Button
//import javafx.scene.control.RadioButton
//import javafx.scene.control.ToggleButton
//import javafx.scene.control.ToggleGroup
//import javafx.scene.image.Image
//import javafx.scene.image.ImageView
//import javafx.scene.layout.HBox
//import model.Model
//
//class ToolBar(private val model: Model): HBox() {
//    init {
//        val addButton = Button("Add Image", ImageView(Image("file:src/main/assets/addImage.png",20.0,20.0,true,true))).apply {
//            onMouseReleased = EventHandler { model.addImage() }
//        }
//        val delButton = Button("Del Image",ImageView(Image("file:src/main/assets/deleteImage.png",20.0,20.0,true,true))).apply {
//            onMouseReleased = EventHandler { model.delImage() }
//            isDisable = true
//        }
//        val rotateLeftButton = Button("Rotate Left",ImageView(Image("file:src/main/assets/rotateLeft.png",20.0,20.0,true,true))).apply {
//            onMouseReleased = EventHandler { model.operate(OperationIndex.ROTATE_LEFT) }
//        }
//        val rotateRightButton = Button("Rotate Right",ImageView(Image("file:src/main/assets/rotateRight.png",20.0,20.0,true,true))).apply {
//            onMouseReleased = EventHandler { model.operate(OperationIndex.ROTATE_RIGHT) }
//        }
//        val zoomInButton = Button("Zoom In",ImageView(Image("file:src/main/assets/zoomIn.png",20.0,20.0,true,true))).apply {
//            onMouseReleased = EventHandler { model.operate(OperationIndex.ZOOM_IN) }
//        }
//        val zoomOutButton = Button("Zoom Out",ImageView(Image("file:src/main/assets/zoomOut.png",20.0,20.0,true,true))).apply {
//            onMouseReleased = EventHandler { model.operate(OperationIndex.ZOOM_OUT) }
//        }
//        val resetButton = Button("Reset",ImageView(Image("file:src/main/assets/reset.png",20.0,20.0,true,true))).apply {
//            onMouseReleased = EventHandler { model.operate(OperationIndex.RESET) }
//        }
//
//        model.SelectedImage.addListener {_,_,newValue ->
//            delButton.isDisable = newValue == null
//        }
//
//        val modeToggleGroup = ToggleGroup().apply {
//            selectedToggleProperty().addListener { _, _, newValue ->
//                newValue as ToggleButton
//                if (newValue.text == "Cascade") model.cascade()
//                else if (newValue.text == "Tile") model.tile()
//                when(newValue.text) {
//                    "Cascade" -> (listOf <Button>(rotateLeftButton, rotateRightButton, zoomInButton, zoomOutButton, resetButton)).forEach {it.isDisable = false}
//                    "Tile" -> (listOf <Button>(rotateLeftButton, rotateRightButton, zoomInButton, zoomOutButton, resetButton)).forEach {it.isDisable = true}
//                }
//            }
//        }
//        val cascadeButton = RadioButton("Cascade").apply {
//            onMouseReleased = EventHandler { model.cascade() }
//            toggleGroup = modeToggleGroup
//        }
//        val tileButton = RadioButton("Tile").apply {
//            onMouseReleased = EventHandler { model.tile() }
//            toggleGroup = modeToggleGroup
//        }
//        modeToggleGroup.selectToggle(modeToggleGroup.toggles.first())
//
//        this.children.addAll(addButton,delButton,rotateLeftButton,rotateRightButton,zoomInButton,zoomOutButton,resetButton)
//        this.children.addAll(cascadeButton,tileButton)
//
//        this.children.forEach {
//            setMargin(it, Insets(5.0))
//        }
//        alignment = Pos.CENTER_LEFT
//    }
//}