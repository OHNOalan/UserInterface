package com.example.pdfreader

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.view.MotionEvent
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
    var isScrollEnabled = true

    var x1 = 0f
    var x2 = 0f
    var y1 = 0f
    var y2 = 0f
    var old_x1 = 0f
    var old_y1 = 0f
    var old_x2 = 0f
    var old_y2 = 0f
    var mid_x = -1f
    var mid_y = -1f
    var old_mid_x = -1f
    var old_mid_y = -1f
    var p1_id = 0
    var p1_index = 0
    var p2_id = 0
    var p2_index = 0

    var transformMatrix = Matrix()
    var inverse = Matrix()
    private fun setupObserver() {
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
        pdfViewModel.brush.removeObserver(brushObserver)
        pdfViewModel.bitmap.removeObserver(bitmapObserver)
        pdfViewModel.paths.removeObserver(pathsObserver)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when(event.pointerCount) {
            1 -> if (isScrollEnabled) true else draw(event) // super.onTouchEvent(event)
            2 -> if(isScrollEnabled) panZoom(event) else true
            else -> return true
        }
    }
    private fun draw(event: MotionEvent) : Boolean {
        val matrix = FloatArray(9)
        transformMatrix.getValues(matrix)

        val tx = matrix[Matrix.MTRANS_X]
        val ty = matrix[Matrix.MTRANS_Y]
        val sx = matrix[Matrix.MSCALE_X]
        val sy = matrix[Matrix.MSCALE_Y]

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path = Path()
                path?.moveTo((event.x - tx) / sx, (event.y - ty) / sy)
                pdfViewModel.setPath(path!!)
            }
            MotionEvent.ACTION_MOVE -> {
                path?.lineTo((event.x - tx) / sx, (event.y - ty) / sy)
                pdfViewModel.setPath(path!!)
            }
            MotionEvent.ACTION_UP -> {
                pdfViewModel.setPath(null)
                pdfViewModel.addPath(path)
                path = null
            }
        }
        return true
    }
    private fun panZoom(event: MotionEvent) : Boolean {
        path = null
        p1_id = event.getPointerId(0)
        p1_index = event.findPointerIndex(p1_id)

        var inverted = floatArrayOf(event.getX(p1_index), event.getY(p1_index))
        inverse.mapPoints(inverted)

        if (old_x1 < 0 || old_y1 < 0) {
            x1 = inverted.get(0)
            old_x1 = x1
            y1 = inverted.get(1)
            old_y1 = y1
        } else {
            old_x1 = x1
            old_y1 = y1
            x1 = inverted.get(0)
            y1 = inverted.get(1)
        }

        p2_id = event.getPointerId(1)
        p2_index = event.findPointerIndex(p2_id)

        inverted = floatArrayOf(event.getX(p2_index), event.getY(p2_index))
        inverse.mapPoints(inverted)

        if (old_x2 < 0 || old_y2 < 0) {
            x2 = inverted.get(0)
            old_x2 = x2
            y2 = inverted.get(1)
            old_y2 = y2
        } else {
            old_x2 = x2
            old_y2 = y2
            x2 = inverted.get(0)
            y2 = inverted.get(1)
        }

        mid_x = (x1 + x2) / 2
        mid_y = (y1 + y2) / 2
        old_mid_x = (old_x1 + old_x2) / 2
        old_mid_y = (old_y1 + old_y2) / 2

        val d_old =
            Math.sqrt(Math.pow((old_x1 - old_x2).toDouble(), 2.0) + Math.pow((old_y1 - old_y2).toDouble(), 2.0))
                .toFloat()
        val d = Math.sqrt(Math.pow((x1 - x2).toDouble(), 2.0) + Math.pow((y1 - y2).toDouble(), 2.0))
            .toFloat()

        if (event.action == MotionEvent.ACTION_MOVE) {
            val dx = mid_x - old_mid_x
            val dy = mid_y - old_mid_y
            transformMatrix.preTranslate(dx, dy)

            var scale = d / d_old
            scale = Math.max(0f, scale)
            transformMatrix.preScale(scale, scale, mid_x, mid_y)
        } else if (event.action == MotionEvent.ACTION_UP) {
            old_x1 = -1f
            old_y1 = -1f
            old_x2 = -1f
            old_y2 = -1f
            old_mid_x = -1f
            old_mid_y = -1f
        }
//        pdfViewModel?.setTransform(currentMatrix)
        return true
    }
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
        when(resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> scaleType = ScaleType.CENTER_INSIDE
            Configuration.ORIENTATION_LANDSCAPE -> scaleType = ScaleType.CENTER_CROP
        }
//        post {
//            when(resources.configuration.orientation) {
//                Configuration.ORIENTATION_PORTRAIT -> {
//                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
//                }
//                Configuration.ORIENTATION_LANDSCAPE -> {
//                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//                }
//            }
//        }
        minimumWidth = 1000
        minimumHeight = 2000

        setupObserver()
//        transformMatrix = Matrix()
    }
}