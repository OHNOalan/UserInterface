package com.example.pdfreader

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pdfreader.model.Brush
import com.example.pdfreader.util.CustomScrollView
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

    lateinit var pdfViewModel : PDFViewModel;

    val LOGNAME = "pdf_viewer"
    val FILENAME = "shannon1948.pdf"
    val FILERESID = R.raw.shannon1948

    // manage the pages of the PDF, see below
    lateinit var pdfRenderer: PdfRenderer
    lateinit var parcelFileDescriptor: ParcelFileDescriptor

    // custom ImageView class that captures strokes and draws them over the image
    lateinit var pageImage: PDFimage

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("oncreate","oncreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pdfViewModel = ViewModelProvider(this, PDFViewModelFactory(resources.displayMetrics.densityDpi))[PDFViewModel::class.java]

        val layout = findViewById<CustomScrollView>(R.id.pdfLayout)
        layout.isEnabled = true
        layout.pdfViewModel = pdfViewModel
        pageImage = PDFimage(this,pdfViewModel)
        layout.addView(pageImage)

        val fileName = findViewById<TextView>(R.id.filename)
        val pageNum = findViewById<TextView>(R.id.pageNum)

        fileName.text = FILENAME
        pdfViewModel.pageNum.observeForever { pageNum.text = it }

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

        pdfViewModel.edit.observeForever {
            when(it) {
                true -> {
                    draw.isEnabled = true
                    highlight.isEnabled = true
                    erase.isEnabled = true
                    undo.isEnabled = true
                    redo.isEnabled = true
                    layout.isScrollEnabled = false
                    edit.text = "view"
                }
                false -> {
                    draw.isEnabled = false
                    highlight.isEnabled = false
                    erase.isEnabled = false
                    undo.isEnabled = false
                    redo.isEnabled = false
                    layout.isScrollEnabled = true
                    edit.text = "edit"
                }
            }
        }

        // open page 0 of the PDF
        // it will be displayed as an image in the pageImage (above)
        try {
            openRenderer(this)
            pdfViewModel.newPDF(pdfRenderer)
        } catch (exception: IOException) {
            Log.d(LOGNAME, "Error opening PDF")
        }
    }

    override fun onStart() {
        Log.d("onstart","onstart called")
        super.onStart()
        try {
            openRenderer(this)
            pdfViewModel.newPDF(pdfRenderer)
        } catch (exception: IOException) {
            Log.d(LOGNAME, "Error opening PDF")
        }
    }

    override fun onStop() {
        Log.d("onstop","onstop called")
        super.onStop()
        try {
            closeRenderer()
        } catch (ex: IOException) {
            Log.d(LOGNAME, "Unable to close PDF renderer")
        }
    }

    @Throws(IOException::class)
    private fun openRenderer(context: Context) {
        // In this sample, we read a PDF from the assets directory.
        val file = File(context.cacheDir, FILENAME)
        if (!file.exists()) {
            // pdfRenderer cannot handle the resource directly,
            // so extract it into the local cache directory.
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
        // capture PDF data
        // all this just to get a handle to the actual PDF representation
        pdfRenderer = PdfRenderer(parcelFileDescriptor)
    }

    @Throws(IOException::class)
    private fun closeRenderer() {
        pdfViewModel.closeRenderer()
        parcelFileDescriptor.close()
    }
}