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

    var currentMatrix = Matrix()
    var inverse = Matrix()

    init {
        pdfViewModel.bitmap.observeForever { bitmap = it }
        pdfViewModel.paths.observeForever {
            paths.clear()
            while(!it.isEmpty()) {
                paths.add(it.pop()!!)
            }
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(event.pointerCount == 2) {
            var inverted = floatArrayOf()
            p1_id = event.getPointerId(0)
            p1_index = event.findPointerIndex(p1_id)

            // mapPoints returns values in-place
            inverted = floatArrayOf(event.getX(p1_index), event.getY(p1_index))
            inverse.mapPoints(inverted)

            // first pass, initialize the old == current value
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

            // point 2
            p2_id = event.getPointerId(1)
            p2_index = event.findPointerIndex(p2_id)

            // mapPoints returns values in-place
            inverted = floatArrayOf(event.getX(p2_index), event.getY(p2_index))
            inverse.mapPoints(inverted)

            // first pass, initialize the old == current value
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

            // midpoint
            mid_x = (x1 + x2) / 2
            mid_y = (y1 + y2) / 2
            old_mid_x = (old_x1 + old_x2) / 2
            old_mid_y = (old_y1 + old_y2) / 2

            // distance
            val d_old =
                Math.sqrt(Math.pow((old_x1 - old_x2).toDouble(), 2.0) + Math.pow((old_y1 - old_y2).toDouble(), 2.0))
                    .toFloat()
            val d = Math.sqrt(Math.pow((x1 - x2).toDouble(), 2.0) + Math.pow((y1 - y2).toDouble(), 2.0))
                .toFloat()

            // pan and zoom during MOVE event
            if (event.action == MotionEvent.ACTION_MOVE) {
    //            Log.d(LOGNAME, "Multitouch move")
                // pan == translate of midpoint
                val dx = mid_x - old_mid_x
                val dy = mid_y - old_mid_y
                currentMatrix.preTranslate(dx, dy)
    //            Log.d(LOGNAME, "translate: $dx,$dy")

                // zoom == change of spread between p1 and p2
                var scale = d / d_old
                scale = Math.max(0f, scale)
                currentMatrix.preScale(scale, scale, mid_x, mid_y)
    //            Log.d(LOGNAME, "scale: $scale")

                // reset on up
            } else if (event.action == MotionEvent.ACTION_UP) {
                old_x1 = -1f
                old_y1 = -1f
                old_x2 = -1f
                old_y2 = -1f
                old_mid_x = -1f
                old_mid_y = -1f
            }
            return true
        }
        else if (event.pointerCount >= 3) {
            // do nothing
            return true
        }
        else {
            // let scrollview handle
            return false
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.concat(currentMatrix)
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
//        minimumHeight = 2000
    }
}