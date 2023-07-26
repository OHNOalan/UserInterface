package com.example.pdfreader.viewModel

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Path
import android.graphics.pdf.PdfRenderer
import android.util.Log
import androidx.core.graphics.values
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pdfreader.model.Brush
import com.example.pdfreader.model.Model
import androidx.lifecycle.ViewModelProvider
import java.util.*

class PDFViewModelFactory(private val resolution: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PDFViewModel::class.java)) {
            return PDFViewModel(resolution) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class PDFViewModel(resolution: Int) : ViewModel() {
    private val model = Model(resolution)

    private var _edit = MutableLiveData(false)
    val edit : LiveData<Boolean> get() { return _edit }
    private val _pageNum = MutableLiveData<String>()
    val pageNum : LiveData<String> get() { return _pageNum }
    private val _bitmap = MutableLiveData<Bitmap?>()
    val bitmap : LiveData<Bitmap?> get() { return _bitmap }
    private val __paths = Stack<Pair<Brush,Path>>()
    private val _paths = MutableLiveData(__paths)
    val paths : LiveData<Stack<Pair<Brush,Path>>> get() { return _paths }
    private var __transformation = Matrix()
    private var _transformation = MutableLiveData(__transformation)
    val transformation : LiveData<Matrix> get() { return _transformation }

    init {
        model.edit.observeForever {
            // need to update paths
            _edit.value = it
        }
        model.pageNum.observeForever {
            // need to update paths
            _pageNum.value = "Page ${it+1}/${model.totalPageNum.value}"
        }
        model.totalPageNum.observeForever {
            // need to update paths
            _pageNum.value = "Page ${model.pageNum}/${it}"
        }
        model.pdfPaths.observeForever { stack ->
            updatePaths(stack)
        }
        model.bitmap.observeForever {bitmap ->
            _bitmap.value = bitmap
            updatePaths(model.pdfPaths.value!!)
        }
    }
    fun setTransform(matrix: Matrix) {
        __transformation = matrix
        _transformation.postValue(__transformation)
    }
    fun closeRenderer() { model.closeRenderer() }
    fun changeBrush(brush: Brush) {
        model.changeBrush(brush)
    }
    fun undo() { model.undo() }
    fun redo() { model.redo() }
    fun lastPage() { model.lastPage() }
    fun nextPage() { model.nextPage() }
    fun newPDF(pdfRenderer: PdfRenderer) { model.newPDF(pdfRenderer) }
    fun edit() { model.edit() }
    fun addPath(path: Path?) {
        if (path != null) model.addPath(path)
    }
    private fun updatePaths(stack: Stack<Pair<Int,Pair<Brush,Path>>>) {
        __paths.clear()
        val paths = stack.filter{ it.first == model.pageNum.value }
        val iter = paths.iterator()
        while (iter.hasNext()) {
            val element = iter.next()
            if(element.second.first == Brush.ERASE) {
                val iterator = __paths.iterator()
                while (iterator.hasNext()) {
                    val item = iterator.next()
                    if (isPathIntersect(item.second,element.second.second)) {
                        iterator.remove() // Remove the item from the stack
                    }
                }
            } else {
                __paths.push(element?.second!!)
            }
        }
        _paths.postValue(__paths)
    }

    private fun isPathIntersect(path1: Path, path2: Path): Boolean {
        val intersectionPath = Path()
        val isIntersected = intersectionPath.op(path1, path2, Path.Op.INTERSECT)
        return isIntersected && !intersectionPath.isEmpty
    }
}