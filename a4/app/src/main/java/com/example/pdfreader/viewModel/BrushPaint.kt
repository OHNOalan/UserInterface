package com.example.pdfreader.viewModel

import android.graphics.Paint
import android.graphics.Color

enum class BrushPaint(val paint: Paint) {
    DRAW(Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }),
    HIGHLIGHT(Paint().apply {
        color = Color.YELLOW
        strokeWidth = 20f
        style = Paint.Style.STROKE
        alpha = 128
    }),
}