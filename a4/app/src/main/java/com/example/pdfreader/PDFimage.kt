package com.example.pdfreader

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import com.example.pdfreader.model.Brush
import com.example.pdfreader.viewModel.BrushPaint
import com.example.pdfreader.viewModel.PDFViewModel

@SuppressLint("AppCompatCustomView", "ViewConstructor")
class PDFimage (context: Context?, pdfViewModel: PDFViewModel) : ImageView(context) {

    private var paths = mutableListOf<Pair<Brush,Path>>()

    private var bitmap: Bitmap? = null
    private var paint = Paint(Color.BLUE)

    var transformMatrix = Matrix()

    init {
        pdfViewModel.bitmap.observeForever { bitmap = it }
        pdfViewModel.paths.observeForever {
            paths.clear()
            while(!it.isEmpty()) {
                paths.add(it.pop()!!)
            }
        }
        pdfViewModel.transformation.observeForever {
            transformMatrix = it
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("PDFImage", "${transformMatrix}")
        canvas.concat(transformMatrix)
        // draw background
        if (bitmap != null) {
            setImageBitmap(bitmap)
        }
        // draw lines over it
        for (path in paths) {
            when (path.first) {
                Brush.DRAW -> {
                    paint = BrushPaint.DRAW.paint
                    canvas.drawPath(path.second, paint)
                }
                Brush.HIGHLIGHT -> {
                    paint = BrushPaint.HIGHLIGHT.paint
                    canvas.drawPath(path.second, paint)
                }
                else -> Unit
            }
        }
        super.onDraw(canvas)
    }
    init {
        minimumWidth = 1000
    }
}