package com.example.pdfreader

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.pdfreader.model.Brush
import com.example.pdfreader.viewModel.BrushPaint
import com.example.pdfreader.viewModel.PDFViewModel
import java.util.*

@SuppressLint("AppCompatCustomView", "ViewConstructor")
class PDFimage (context: Context?) : ImageView(context) {
    private val pdfViewModel : PDFViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(PDFViewModel::class.java)
    private val transformationObserver = Observer<Matrix> { transformMatrix = it }
    private val pathObserver = Observer<Path?> { path = it }
    private val brushObserver = Observer<Brush> { brush = it }
    private val bitmapObserver = Observer<Bitmap?> { bitmap = it }
    private val pathsObserver = Observer<Stack<Pair<Brush, Path>>> {
        paths.clear()
        while(!it.isEmpty()) {
            paths.add(it.pop()!!)
        }
    }
    private var paths = mutableListOf<Pair<Brush,Path>>()
    private var bitmap: Bitmap? = null
    private var brush = Brush.DRAW
    private var path : Path? = null
    private var transformMatrix : Matrix
    init {
        setupObserver()
        transformMatrix = Matrix()
    }
    private fun setupObserver() {
        pdfViewModel.transformation.observeForever(transformationObserver)
        pdfViewModel.path.observeForever(pathObserver)
        pdfViewModel.brush.observeForever(brushObserver)
        pdfViewModel.bitmap.observeForever(bitmapObserver)
        pdfViewModel.paths.observeForever(pathsObserver)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupObserver()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pdfViewModel.transformation.removeObserver(transformationObserver)
        pdfViewModel.path.removeObserver(pathObserver)
        pdfViewModel.brush.removeObserver(brushObserver)
        pdfViewModel.bitmap.removeObserver(bitmapObserver)
        pdfViewModel.paths.removeObserver(pathsObserver)
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
        post {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        minimumWidth = resources.displayMetrics.widthPixels
        minimumHeight = resources.displayMetrics.heightPixels
    }
}