package com.example.pdfreader.util

import android.content.Context
import android.graphics.Matrix
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ScrollView
import com.example.pdfreader.R
import com.example.pdfreader.viewModel.PDFViewModel

class CustomScrollView : ScrollView {
    private val LOGNAME = "pdf_image"
    private var path: Path? = null
    var isScrollEnabled = true
    var pdfViewModel: PDFViewModel? = null
//    private var scaleGestureDetector: ScaleGestureDetector? = null
//    private var scaleFactor = 1.0f

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

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

//    init {
//        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
//    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isScrollEnabled) {
            when (event.pointerCount) {
                1 -> return super.onTouchEvent(event)
                2 -> {
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
                        Log.d(LOGNAME, "Multitouch move")
                        // pan == translate of midpoint
                        val dx = mid_x - old_mid_x
                        val dy = mid_y - old_mid_y
                        currentMatrix.preTranslate(dx, dy)
                        Log.d(LOGNAME, "translate: $dx,$dy")

                        // zoom == change of spread between p1 and p2
                        var scale = d / d_old
                        scale = Math.max(0f, scale)
                        currentMatrix.preScale(scale, scale, mid_x, mid_y)
                        Log.d(LOGNAME, "scale: $scale")

                        // reset on up
                    } else if (event.action == MotionEvent.ACTION_UP) {
                        old_x1 = -1f
                        old_y1 = -1f
                        old_x2 = -1f
                        old_y2 = -1f
                        old_mid_x = -1f
                        old_mid_y = -1f
                    }
                    val matrixValues = FloatArray(9)
                    matrix.getValues(matrixValues)
                    scaleX = matrixValues[Matrix.MSCALE_X]
                    scaleY = matrixValues[Matrix.MSCALE_Y]
                    x = matrixValues[Matrix.MTRANS_X]
                    y = matrixValues[Matrix.MTRANS_Y]
                    return true
                }
                else -> return false
            }
        } else {
            // need to check edit state later on
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(LOGNAME, "Action down")
                    path = Path()
                    path?.moveTo(event.x + scrollX, event.y + scrollY)
                }

                MotionEvent.ACTION_MOVE -> {
                    Log.d(LOGNAME, "Action move")
                    path?.lineTo(event.x + scrollX, event.y + scrollY)
                }

                MotionEvent.ACTION_UP -> {
                    Log.d(LOGNAME, "Action up")
                    pdfViewModel?.addPath(path)
                }
            }
            return true
        }
    }

//    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
//        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
//            Log.d("scaleBegin", "scaleBegin")
//            return super.onScaleBegin(detector)
//        }
//        override fun onScale(detector: ScaleGestureDetector): Boolean {
//            scaleFactor *= detector.scaleFactor
//            Log.d("deScale", "descalefactor is ${detector.scaleFactor}")
//            scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 3.0f))
//            Log.d("Scale", "scalefactor is $scaleFactor")
//            scaleX = scaleFactor
//            scaleY = scaleFactor
//            return true
//        }
//        override fun onScaleEnd(detector: ScaleGestureDetector) {
//            if(scaleFactor >= 3.0f) {
//                scaleFactor = 2.99f
//            }
//            if(scaleFactor < 1.0f) {
//                scaleFactor = 1.0f
//            }
//        }
//    }
}