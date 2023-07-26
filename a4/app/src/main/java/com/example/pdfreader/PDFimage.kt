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
    private var brush = Brush.DRAW
    private var path : Path? = null
    var transformMatrix = Matrix()
    init {
        pdfViewModel.bitmap.observeForever { bitmap = it }
        pdfViewModel.paths.observeForever {
            paths.clear()
            while(!it.isEmpty()) {
                paths.add(it.pop()!!)
            }
        }
        pdfViewModel.transformation.observeForever { transformMatrix = it }
        pdfViewModel.path.observeForever { path = it }
        pdfViewModel.brush.observeForever { brush = it }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean { return false }

    override fun onDraw(canvas: Canvas) {
        canvas.concat(transformMatrix)
        if (bitmap != null) setImageBitmap(bitmap)
        for (path in paths) {
            when (path.first) {
                Brush.DRAW -> {
                    val paint = BrushPaint.DRAW.paint
                    canvas.drawPath(path.second, paint)
                }
                Brush.HIGHLIGHT -> {
                    val paint = BrushPaint.HIGHLIGHT.paint
                    canvas.drawPath(path.second, paint)
                }
                else -> Unit
            }
        }
        if(path != null) {
            when(brush) {
                Brush.DRAW -> canvas.drawPath(path!!, BrushPaint.DRAW.paint)
                Brush.HIGHLIGHT -> canvas.drawPath(path!!, BrushPaint.HIGHLIGHT.paint)
                else -> Unit
            }
        }
        super.onDraw(canvas)
    }
    init {
        minimumWidth = 1000
    }
}