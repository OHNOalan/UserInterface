package com.example.pdfreader

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pdfreader.model.Brush
import com.example.pdfreader.viewModel.PDFViewModel
import com.example.pdfreader.viewModel.PDFViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// PDF sample code from
// https://medium.com/@chahat.jain0/rendering-a-pdf-document-in-android-activity-fragment-using-pdfrenderer-442462cb8f9a
// Issues about cache etc. are not at all obvious from documentation, so we should expect people to need this.
// We may wish to provide this code.
class MainActivity : AppCompatActivity() {

    lateinit var pdfViewModel : PDFViewModel

    val LOGNAME = "pdf_viewer"
    val FILENAME = "shannon1948.pdf"
    val FILERESID = R.raw.shannon1948

    lateinit var pdfRenderer: PdfRenderer
    lateinit var parcelFileDescriptor: ParcelFileDescriptor

    lateinit var pageImage: PDFimage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pdfViewModel = ViewModelProvider(this, PDFViewModelFactory(resources.displayMetrics.densityDpi))[PDFViewModel::class.java]

        val layout = findViewById<LinearLayout>(R.id.pdfLayout)
        layout.isEnabled = true
        pageImage = PDFimage(this)
        layout.addView(pageImage)

        val fileName = findViewById<TextView>(R.id.filename)
        val pageNum = findViewById<TextView>(R.id.pageNum)

        fileName.text = FILENAME
        pdfViewModel.pageNum.observe(this) { pageNum.text = it }

        val draw = findViewById<ImageButton>(R.id.draw)
        val highlight = findViewById<ImageButton>(R.id.highlight)
        val erase = findViewById<ImageButton>(R.id.erase)
        val undo = findViewById<ImageButton>(R.id.undo)
        val redo = findViewById<ImageButton>(R.id.redo)
        val last = findViewById<Button>(R.id.prev)
        val next = findViewById<Button>(R.id.next)
        val edit = findViewById<Button>(R.id.edit)

        draw.setOnClickListener { pdfViewModel.changeBrush(Brush.DRAW) }
        highlight.setOnClickListener { pdfViewModel.changeBrush(Brush.HIGHLIGHT) }
        erase.setOnClickListener { pdfViewModel.changeBrush(Brush.ERASE) }
        undo.setOnClickListener { pdfViewModel.undo() }
        redo.setOnClickListener { pdfViewModel.redo() }
        last.setOnClickListener { pdfViewModel.lastPage() }
        next.setOnClickListener { pdfViewModel.nextPage() }
        edit.setOnClickListener { pdfViewModel.edit() }

        pdfViewModel.edit.observe(this) {
            if(it) {
                draw.isEnabled = true; draw.alpha = 1.0f
                highlight.isEnabled = true; highlight.alpha = 1.0f
                erase.isEnabled = true; erase.alpha = 1.0f
                undo.isEnabled = true; undo.alpha = 1.0f
                redo.isEnabled = true; redo.alpha = 1.0f
                pageImage.isScrollEnabled = false
                edit.text = "view"
            } else {
                draw.isEnabled = false; draw.alpha = 0.5f
                highlight.isEnabled = false; highlight.alpha = 0.5f
                erase.isEnabled = false; erase.alpha = 0.5f
                undo.isEnabled = false; undo.alpha = 0.5f
                redo.isEnabled = false; redo.alpha = 0.5f
                pageImage.isScrollEnabled = true
                edit.text = "edit"
            }
        }

        try {
            openRenderer(this)
            pdfViewModel.newPDF(pdfRenderer)
        } catch (exception: IOException) {
            Log.d(LOGNAME, "Error opening PDF")
        }
    }

//    override fun onStop() {
//        super.onStop()
//        try {
//            closeRenderer()
//            findViewById<ImageButton>(R.id.draw).setOnClickListener(null)
//            findViewById<ImageButton>(R.id.highlight).setOnClickListener(null)
//            findViewById<ImageButton>(R.id.erase).setOnClickListener(null)
//            findViewById<ImageButton>(R.id.undo).setOnClickListener(null)
//            findViewById<ImageButton>(R.id.redo).setOnClickListener(null)
//            findViewById<Button>(R.id.prev).setOnClickListener(null)
//            findViewById<Button>(R.id.next).setOnClickListener(null)
//            findViewById<Button>(R.id.edit).setOnClickListener(null)
//
//
//        } catch (ex: IOException) {
//            Log.d(LOGNAME, "Unable to close PDF renderer")
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            closeRenderer()
            findViewById<ImageButton>(R.id.draw).setOnClickListener(null)
            findViewById<ImageButton>(R.id.highlight).setOnClickListener(null)
            findViewById<ImageButton>(R.id.erase).setOnClickListener(null)
            findViewById<ImageButton>(R.id.undo).setOnClickListener(null)
            findViewById<ImageButton>(R.id.redo).setOnClickListener(null)
            findViewById<Button>(R.id.prev).setOnClickListener(null)
            findViewById<Button>(R.id.next).setOnClickListener(null)
            findViewById<Button>(R.id.edit).setOnClickListener(null)


        } catch (ex: IOException) {
            Log.d(LOGNAME, "Unable to close PDF renderer")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt("PageNum", pdfViewModel.pNum.value?:0)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        with(savedInstanceState) {
            // if(getInt("Edit") == 1) pdfViewModel.edit()
            pdfViewModel.setPage(getInt("PageNum"))
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    @Throws(IOException::class)
    private fun openRenderer(context: Context) {
        val file = File(context.cacheDir, FILENAME)
        if (!file.exists()) {
            val asset = this.resources.openRawResource(FILERESID)
            val output = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var size: Int
            while (asset.read(buffer).also { size = it } != -1) {
                output.write(buffer, 0, size)
            }
            asset.close()
            output.close()
        }
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        pdfRenderer = PdfRenderer(parcelFileDescriptor)
    }

    @Throws(IOException::class)
    private fun closeRenderer() {
        pdfViewModel.closeRenderer()
        parcelFileDescriptor.close()
    }
}