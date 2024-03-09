package com.rf.geolgy.utils

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.pdf.PrintedPdfDocument
import java.io.FileOutputStream
import java.io.IOException


class MyPrintDocumentAdapter(private val context: Context) : PrintDocumentAdapter() {
    override fun onLayout(
        oldAttributes: PrintAttributes,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal,
        callback: LayoutResultCallback,
        extras: Bundle?
    ) {
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled()
            return
        }
        val builder = PrintDocumentInfo.Builder("file_name.pdf")
        builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
            .build()
        callback.onLayoutFinished(builder.build(), newAttributes != oldAttributes)
    }

    override fun onWrite(
        pages: Array<PageRange?>?,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback
    ) {

        val printAttributes = PrintAttributes.Builder()
            .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .setResolution(PrintAttributes.Resolution("pdf", "pdf", 600, 600))
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            .build()

        val pdfDocument = PrintedPdfDocument(context, printAttributes)
        val page = pdfDocument.startPage(0)

        // Draw your content on the page
        // For example:
        // Canvas canvas = page.getCanvas();
        // canvas.drawText("Hello, World!", 100, 100, new Paint());
        pdfDocument.finishPage(page)
        try {
            val outputStream = FileOutputStream(destination.fileDescriptor)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.close()
            callback.onWriteFinished(arrayOf<PageRange>(PageRange.ALL_PAGES))
        } catch (e: IOException) {
            callback.onWriteFailed(e.toString())
        }
    }
}

