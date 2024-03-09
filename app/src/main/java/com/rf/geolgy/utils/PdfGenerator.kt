package com.rf.geolgy.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintAttributes.Resolution
import android.print.pdf.PrintedPdfDocument
import android.webkit.WebView
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object PdfGenerator {
    fun generatePdfFromWebView(context: Context, webView: WebView, fileName: String?) {
        // Create a bitmap of the WebView content
        val bitmap =
            Bitmap.createBitmap(webView.width, webView.contentHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        webView.draw(canvas)

        // Create a PDF document
        val pdfDocument = PrintedPdfDocument(
            context, PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(Resolution("pdf", "pdf", 600, 600))
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build()
        )
        val pageInfo = PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        // Draw the bitmap on the PDF page
        val pdfCanvas = page.canvas
        pdfCanvas.drawBitmap(bitmap, 0f, 0f, null)

        // Finish the page
        pdfDocument.finishPage(page)

        // Save the PDF document to a file
        try {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
            val outputStream = FileOutputStream(file)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.close()

            sendPdfViaWhatsApp(context, file, "7837709702");
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun sendPdfViaWhatsApp(context: Context, file: File, recipientPhoneNumber: String) {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(
            Intent.EXTRA_STREAM,
            FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".fileprovider",
                file
            )
        )
        sendIntent.setType("application/pdf")
        sendIntent.putExtra("jid", "$recipientPhoneNumber@s.whatsapp.net")
        sendIntent.setPackage("com.whatsapp")
        context.startActivity(sendIntent)
    }
}