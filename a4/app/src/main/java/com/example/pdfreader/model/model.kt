package com.example.pdfreader.model

import android.graphics.Bitmap
import android.graphics.Path
import android.graphics.pdf.PdfRenderer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class Model(private val resolution: Int) {
    // interface
    private var _edit = MutableLiveData(false)
    val edit : LiveData<Boolean> get() { return _edit }
    private var __brush = Brush.DRAW
    private val _brush = MutableLiveData(__brush)
    val brush : LiveData<Brush> get() { return _brush }
    private val _pageNum = MutableLiveData(0)
    val pageNum : LiveData<Int> get() { return _pageNum }
    private val _totalPageNum = MutableLiveData(0)
    val totalPageNum : LiveData<Int> get() { return _totalPageNum }
    private val _bitmap = MutableLiveData<Bitmap?>()
    val bitmap : LiveData<Bitmap?> get() { return _bitmap }
    private val _pdfpaths = Stack<Pair<Int, Pair<Brush,Path>>>()
    private val _pdfPaths = MutableLiveData(_pdfpaths)
    val pdfPaths : LiveData<Stack<Pair<Int, Pair<Brush, Path>>>> get() { return _pdfPaths }

    // private variable
    private var pdfRenderer : PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null
    private var pathRedoStack = Stack<Pair<Int,Pair<Brush,Path>>>()

    fun nextPage() {
        if(_pageNum.value!! < totalPageNum.value!! - 1) {
            _pageNum.value = pageNum.value!! + 1
            updateBitMap()
        }
    }
    fun lastPage() {
        if(_pageNum.value!! > 0) {
            _pageNum.value = pageNum.value!! - 1
            updateBitMap()
        }
    }
    fun setPage(index: Int) {
        if(0 <= index && index < totalPageNum.value!!) {
            _pageNum.value = index
            updateBitMap()
        }
    }

    // bitmap need to be listened
    private fun updateBitMap() {
        if(pdfRenderer != null) {
            val currentPage = pdfRenderer!!.openPage(pageNum.value!!)
            if (currentPage != null) {
                val bitmap = Bitmap.createBitmap( resolution * currentPage.width / 72, resolution * currentPage.height / 72, Bitmap.Config.ARGB_8888)
//                val bitmap = Bitmap.createBitmap( currentPage.width, currentPage.height, Bitmap.Config.ARGB_8888)
                currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                _bitmap.value = bitmap
            }
            currentPage.close()
        }
    }

    fun closeRenderer(){
        currentPage?.close()
        currentPage = null
        pdfRenderer?.close()
        pdfRenderer = null
    }
    fun addPath(path: Path) {
        _pdfpaths.push(Pair(_pageNum.value!!,Pair(__brush,path)))
        pathRedoStack.clear()
        _pdfPaths.postValue(_pdfpaths)
    }
    fun undo() {
        if(!_pdfpaths.isEmpty()) {
            val path = _pdfpaths.pop()
            if (path != null) {
                pathRedoStack.push(path)
                _pageNum.value = path.first
                updateBitMap()
            }
            _pdfPaths.postValue(_pdfpaths)
        }
    }

    fun redo() {
        if(!pathRedoStack.isEmpty()) {
            val path = pathRedoStack.pop()
            if (path != null) {
                _pdfpaths.push(path)
                _pageNum.value = path.first
                updateBitMap()
            }
            _pdfPaths.postValue(_pdfpaths)
        }
    }
    fun changeBrush(brush: Brush) {
        __brush = brush
        _brush.postValue(__brush)
    }
    fun edit() { _edit.value = !_edit.value!! }

    fun newPDF(pdfRenderer: PdfRenderer) {
        this.pdfRenderer = pdfRenderer
        _totalPageNum.value = pdfRenderer.pageCount
        _pageNum.value = 0
        updateBitMap()
    }
}