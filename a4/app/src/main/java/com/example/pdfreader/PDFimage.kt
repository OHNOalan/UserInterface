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

@SuppressLint("AppCompatCustomView")
class PDFimage  // constructor
    (context: Context?, val pdfViewModel: PDFViewModel) : ImageView(context) {
    val LOGNAME = "pdf_image"

    // drawing path
    var path: Path? = null
    var paths = mutableListOf<Pair<Brush,Path>>()

    // image to display
    var bitmap: Bitmap? = null
    var paint = Paint(Color.BLUE)

    init {
        pdfViewModel.bitmap.observeForever {
            bitmap = it
        }
        pdfViewModel.paths.observeForever {
            paths.clear()
            while(!it.isEmpty()) {
                paths.add(it.pop()!!)
            }
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // drawing is apply on scrollview
        return false
    }

    fun setBrush(paint: Paint) {
        this.paint = paint
    }

    override fun onDraw(canvas: Canvas) {
        // draw background
        if (bitmap != null) {
            setImageBitmap(bitmap)
        }
//        Log.d("PathNUM", "${paths.size} paths in total")
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
        minimumWidth = 2000
        minimumHeight = 2000
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
    }
}