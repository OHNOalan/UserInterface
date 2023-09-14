package ui.lectures.javafx.mvc.propertiesmvcextended.model

import javafx.scene.layout.*
import javafx.scene.paint.Color

const val borderWidth = 2.0
const val cornerRadii = 2.0

val BorderObject = Border(
    BorderStroke(
        Color.AQUA,
        BorderStrokeStyle.SOLID, CornerRadii(cornerRadii), BorderWidths(borderWidth)
    )
)

const val imagePrefWidthMax = 250.0
const val imagePrefHeightMax = 250.0