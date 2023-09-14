package components.FileContent

import javafx.geometry.Pos
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Text
import java.io.File
import java.io.FileReader


class ContentDisplay : VBox() {
    private var dirPath = ""

    init {
        this.apply {
            border = Border(
                BorderStroke(
                    Color.rgb(200,200,200),
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT
                )
            )
            alignment = Pos.CENTER
        }
        }

    fun changeDirPath(path: String){
        dirPath = path
    }

    fun select(fileName: String){
        this.children.clear()
        if (fileName == "") {
            this.children.add(Text("Preview of selected file"))
        } else{
            val filePath = dirPath + fileName
            val file = File(filePath)
            if(file.isDirectory){
//                this.children.add(Text("You select a directory, double click if you want to proceed."))
            } else {
                val lastDotPos = fileName.lastIndexOf(".")
                if(lastDotPos == -1){
                    this.children.add(Text("Unsupported type"))
                } else {
                    val ext = fileName.substring(lastDotPos + 1, fileName.length)
//                    this.children.add(Text("You select a file with \"$ext\" extension."))
                    if(ext == "png" || ext == "jpg" || ext == "bmp"){
                        if(file.canRead()){
                            val imageUrl = file.toURI().toURL().toString()
                            val image = Image(imageUrl,this.width-10,this.height-10,true,false)
                            val imageView = ImageView(image)
                            this.children.add(imageView)
                        } else{
                            this.children.add(Text("File cannot be read"))
                        }
                    } else if(ext == "txt" || ext == "md"){
                        if(file.canRead()){
                            val reader = FileReader(File(filePath))
                            this.children.add(ScrollPane().apply { content = Text(reader.readText()) })
                        } else{
                            this.children.add(Text("File cannot be read"))
                        }
                    } else {
                        this.children.add(Text("Unsupported type"))
                    }
                }
            }
        }
    }
}