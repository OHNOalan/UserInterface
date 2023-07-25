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