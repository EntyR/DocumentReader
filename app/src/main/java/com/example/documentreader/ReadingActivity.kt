package com.example.documentreader

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.appcompat.app.AppCompatActivity
import com.example.documentreader.databinding.ActivityReadingBinding
import java.io.File
import java.net.URI




class ReadingActivity : AppCompatActivity() {

    lateinit var binding: ActivityReadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val display = windowManager.defaultDisplay;
        val outSize = Point()
        display.getSize(outSize)
        val x = outSize.x
        val y = outSize.y


        val bundle = intent.extras
        var file: File? = null

        bundle?.let {
            val netUri: Uri? = Uri.parse(it.getString("document"))
            netUri?.let {
                val uri = URI(it.toString())
                file = File(uri)
            }
        }

        val parcelerDescriptor = ParcelFileDescriptor.open(
            file,
            ParcelFileDescriptor.MODE_READ_ONLY
        )
        val pdfRender = PdfRenderer(parcelerDescriptor)



        val pages = pdfRender.pageCount
        val bitmap = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888)
        for (i in 0 until pages){
            val page = pdfRender.openPage(i)

            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            binding.imageView.setImageBitmap(bitmap)
            page.close()
        }
        pdfRender.close()


    }
}