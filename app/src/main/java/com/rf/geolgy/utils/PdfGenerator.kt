package com.rf.geolgy.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.StaticLayout
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfName.Link
import com.itextpdf.kernel.pdf.action.PdfAction
import com.itextpdf.kernel.pdf.annot.PdfAnnotation
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.rf.geolgy.R
import com.rf.geolgy.data.model.request.CreateChallanRequest
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.EnumMap

object PdfGenerator {
    private var currentValue: Float = 0f
    fun createPdf(context: Activity, request: CreateChallanRequest, licenceType: String?) {
        // Create a new PdfDocument
        val document = PdfDocument()
        // Create a page description
        val pageInfo: PdfDocument.PageInfo =
            PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size: 595 x 842 points
        // Start a page
        val page: PdfDocument.Page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 12F
            isAntiAlias = true
        }

        val boldPaint = Paint().apply {
            color = Color.BLACK // Set text color
            textSize = 12f // Set text size
            isAntiAlias = true // Enable anti-aliasing for smoother text
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // Set typeface to bold
        }
        val watermarkText = "https://geologyminingjk.gov.in/ https://geologyminingjk.gov.in/" // Watermark text
        val watermarkPaint = TextPaint().apply {
            color = Color.GRAY
            textSize = 12f
            alpha = 50 // Set transparency (0-255), adjust as needed
        }


        val textHeight = -watermarkPaint.ascent() + watermarkPaint.descent()

        // Calculate the number of repetitions needed vertically
        val numRepetitions = pageInfo.pageHeight.toFloat() / textHeight
        val rect = Rectangle(36f, 700f, 200f, 16f)

        // Draw the watermark text repeatedly from top to bottom
        for (i in 0 until numRepetitions.toInt()) {
            val startY = (i + 1) * textHeight // Adjust position for each repetition
            canvas.drawText(watermarkText, 120F, startY, watermarkPaint) // Adjust X position as needed
        }

        val govtText: Float = (pageInfo.pageWidth - paint.measureText(
            ContextCompat.getString(
                context,
                R.string.government_of_jammu_kashmir
            )
        )) / 2
        val departmentOf: Float = (pageInfo.pageWidth - paint.measureText(
            ContextCompat.getString(
                context,
                R.string.department_of
            )
        )) / 2
        val form: Float = (pageInfo.pageWidth - paint.measureText(
            ContextCompat.getString(
                context,
                R.string.form
            )
        )) / 2
        val seeRule: Float =
            (pageInfo.pageWidth - paint.measureText("[See Rule 38(5), 50(12), 60(1)(v), 70, 71]")) / 2
        val seeRule2: Float =
            (pageInfo.pageWidth - paint.measureText("of challan for dispatch of mineral and its products")) / 2
        val eChallan: Float = (pageInfo.pageWidth - paint.measureText(
            ContextCompat.getString(
                context,
                R.string.e_challan
            )
        )) / 2

        // Add content to the page
        canvas.drawText(
            ContextCompat.getString(context, R.string.government_of_jammu_kashmir),
            govtText,
            30F,
            boldPaint
        )
        canvas.drawText(
            ContextCompat.getString(context, R.string.department_of),
            departmentOf,
            50F,
            boldPaint
        )
        canvas.drawText(ContextCompat.getString(context, R.string.form), form, 70F, boldPaint)
        canvas.drawText("[See Rule 38(5), 50(12), 60(1)(v), 70, 71]", seeRule, 90F, boldPaint)
        canvas.drawText(
            "of challan for dispatch of mineral and its products",
            seeRule2,
            110F,
            boldPaint
        )
        canvas.drawText(
            ContextCompat.getString(context, R.string.e_challan),
            eChallan,
            130F,
            boldPaint
        )
        val challanNumber: Float = (pageInfo.pageWidth - paint.measureText(
            "Challan No. JK08Y-${request.challanNumber}"
        )) / 2
        canvas.drawText(
            "Challan No. JK08Y-${request.challanNumber}",
            challanNumber,
            150F,
            boldPaint
        )

        // Add QR code image to PDF
        val qrCodeBitmap = generateQRCode("qrText", context)
        val byteArrayOutputStream = ByteArrayOutputStream()
        qrCodeBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        // Add QR code image to PDF
        val imageBitmap: Bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val destRect = Rect(20, 170, 80, 230)
        canvas.drawBitmap(imageBitmap, null, destRect, null)
        val validFrom: Float = (pageInfo.pageWidth - paint.measureText(
            "Validity from ${request.validFrom} to ${request.validTo}"
        )) / 2
        canvas.drawText(
            "Validity from ${request.validFrom} to ${request.validTo}",
            validFrom,
            250F,
            boldPaint
        )
        val point1 =
            paint.measureText("1. Type of mineral concessions Lease / License / Permit no. ")
        currentValue = 270F
        canvas.drawText(
            "1. Type of mineral concessions Lease / License / Permit no.",
            20f,
            currentValue,
            paint
        )
        canvas.drawText("$licenceType", 20f + point1, currentValue, boldPaint)
        createPointTwo1(canvas, paint, request, boldPaint, getNextValue(24F))

        canvas.drawText(
            "2. Name & Style of Concessionary .......................................",
            20f,
            getNextValue(24F),
            paint
        )
        canvas.drawText(
            "3. Location of mineral concession area .................................",
            20f,
            getNextValue(24F),
            paint
        )
        canvas.drawText(
            "4. Type of mineral Granted on mineral concessions .................................",
            20f,
            getNextValue(24F),
            paint
        )
        canvas.drawText(
            "5. Quantity of mineral granted on mineral Concessions .................................",
            20f,
            getNextValue(24F),
            paint
        )

        val point6 = paint.measureText("6. Name & Location of Stone crusher Unit and Holder ")
        canvas.drawText(
            "6. Name & Location of Stone crusher Unit and Holder",
            20f,
            getNextValue(24F),
            paint
        )
        canvas.drawText("${request.nameAndLocation}", 20f + point6, currentValue, boldPaint)

        val point7 = paint.measureText("7. Type of Finished Products: ")
        canvas.drawText("7. Type of Finished Products:", 20f, getNextValue(24F), paint)
        canvas.drawText("${request.product}", 20f + point7, currentValue, boldPaint)

        val point8 = paint.measureText("8. Quantity of mineral dispatched ")
        canvas.drawText("8. Quantity of mineral dispatched", 20f, getNextValue(24F), paint)
        canvas.drawText("${request.quantityDispatched}", 20f + point8, currentValue, boldPaint)

        createPoint9(canvas, paint, request, boldPaint, getNextValue(24F))
        createPoint10(canvas, paint, request, boldPaint, getNextValue(24F))
        // Measure the width of the regular parts of the text

        canvas.drawText(
            "11. Rate of Mineral GST Rs. $1 % Total Amount (Excluding GST and Transportation charges) Rs. %2",
            20f,
            getNextValue(24F),
            paint
        )

        canvas.drawText(
            "12. GST Bill/No. ${request.gstNumber} Quantity 5.0% Amount Rs.%2 (Enclose copy of GST Invoice)",
            20f,
            getNextValue(24F),
            paint
        )
        createPoint13(canvas, paint, request, boldPaint, getNextValue(24F))
        createPoint14(canvas, paint, request, boldPaint, getNextValue(24F))
        createPoint15(canvas, paint, request, boldPaint, getNextValue(24F))

        canvas.drawText(
            "Note: The Information mentioned in e-Challan, Such as (Validity and Vehicle No.) should be matched",
            20f,
            getNextValue(25F),
            boldPaint
        )
        canvas.drawText(
            "with the information mentioned in the https://geologymining.jk.gov.in/ which can be seen after scanning",
            20f,
            getNextValue(15F),
            boldPaint
        )
        canvas.drawText("the QR code encrypted on e-Challan.", 20f, getNextValue(15F), boldPaint)

        val paintBottomBox = Paint()
        paint.color = Color.BLACK
        paint.textSize = 9F
        canvas.drawText(
            "Self Approved by Mineral Concessionary",
            30f,
            725F,
            paintBottomBox
        )
        canvas.drawText(
            "Signature & Seal of Mineral Concessionary",
            320f,
            725F,
            paintBottomBox
        )

        val fillPaint = Paint().apply {
            color = Color.TRANSPARENT // Set color with 50% transparency (128 out of 255)
            style = Paint.Style.FILL
        }

        // Create a custom Paint object for the stroke color
        val strokePaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
        }

        // Draw the transparent rectangle with black boundary
        val destRect1 = Rect(20, 710, 290, 830)
        val destRect2 = Rect(310, 710, 580, 830)
        canvas.drawRect(destRect1, fillPaint)
        canvas.drawRect(destRect1, strokePaint)
        canvas.drawRect(destRect2, fillPaint)
        canvas.drawRect(destRect2, strokePaint)

        // Load the image from drawable resources
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.image)
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val destRectImage = Rect(30, 735, 110, 810)
        canvas.drawBitmap(bitmap, null, destRectImage, null)

        // Finish the page
        document.finishPage(page)
        // Save the document
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "example.pdf")
        try {
            val fos = FileOutputStream(file)
            document.writeTo(fos)
            fos.close()
            // Close the document
            document.close()
            openPdfFile(context, file);
            // Show the PDF
            //showPdf(context, file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createPoint13(
        canvas: Canvas, paint: Paint, request: CreateChallanRequest, boldPaint: Paint,
        position: Float
    ) {
        val point13 = paint.measureText("13. Vehicle No. ")
        // Draw the regular parts of the text
        canvas.drawText("13. Vehicle No. ", 20f, position, paint)
        canvas.drawText("${request.vehicleNo}", 20f + point13, position, boldPaint)

    }

    private fun createPoint14(
        canvas: Canvas, paint: Paint, request: CreateChallanRequest, boldPaint: Paint,
        position: Float
    ) {
        val point14 = paint.measureText("14. Name & Address of Consignee / Buyer / Purchaser ")
        // Draw the regular parts of the text
        canvas.drawText("14. Name & Address of Consignee / Buyer / Purchase ", 20f, position, paint)
        canvas.drawText("${request.nameAndAddress}", 20f + point14, position, boldPaint)

    }

    private fun createPoint15(
        canvas: Canvas, paint: Paint, request: CreateChallanRequest, boldPaint: Paint,
        position: Float
    ) {
        val point15 = paint.measureText("15. Name & Phone No. of Driver  ")
        // Draw the regular parts of the text
        canvas.drawText("15. Name & Phone No. of Driver ", 20f, position, paint)
        canvas.drawText(
            "${request.driverName} , ${request.driverPhone}",
            20f + point15,
            position,
            boldPaint
        )

    }

    private fun createPoint10(
        canvas: Canvas,
        paint: Paint,
        request: CreateChallanRequest,
        boldPaint: Paint,
        position: Float
    ) {
        val startX = 20f

        val text =
            "10. Route of the Transportation- Source ${request.routeSource} Destination ${request.routeDesignation}"
        val textWidth = paint.measureText(text)
        Log.i("Width", "Max : $textWidth")
        if (textWidth > 590)
        {
            canvas.drawText("10. Route of the Transportation- Source ${request.routeSource}", startX, position, boldPaint)
            canvas.drawText("Destination ${request.routeDesignation}", 40F, getNextValue(15F), boldPaint)
        }else
        {
            canvas.drawText(text, startX, position, boldPaint)
        }
    }

    private fun createPointTwo1(
        canvas: Canvas,
        paint: Paint,
        request: CreateChallanRequest,
        boldPaint: Paint,
        position: Float
    ) {

        val point1 = paint.measureText("Issuing date ")
        val pointTwo1 = paint.measureText(" Valid upto ")
        val boldWidthFrom = boldPaint.measureText(request.validFrom)
        val boldWidthTo = boldPaint.measureText(request.validTo)
        // Draw the regular parts of the text

        canvas.drawText("Issuing date ", 35f, position, paint)

        // Calculate the width of the regular and bold parts combined
        val totalWidthBeforeFrom = point1 + boldWidthFrom
        val totalWidthBetween = pointTwo1 + boldWidthTo

        // Draw the bold part "request.validFrom"
        canvas.drawText(request.validFrom!!, 35f + point1, position, boldPaint)

        // Draw the regular part " to "
        canvas.drawText(" Valid upto ", 35f + totalWidthBeforeFrom, position, paint)

        // Draw the bold part "request.validTo"
        canvas.drawText(
            request.validTo!!,
            35f + totalWidthBeforeFrom + pointTwo1,
            position,
            boldPaint
        )
    }

    private fun createPoint9(
        canvas: Canvas,
        paint: Paint,
        request: CreateChallanRequest,
        boldPaint: Paint,
        position: Float
    ) {
        val point9 = paint.measureText("9. DATE & TIME of dispatch ")
        val pointTwo9 = paint.measureText(" to ")
        // Calculate the width of the bold parts of the text
        val boldWidthFrom = boldPaint.measureText(request.validFrom)
        val boldWidthTo = boldPaint.measureText(request.validTo)
        // Draw the regular parts of the text
        canvas.drawText("9. DATE & TIME of dispatch ", 20f, position, paint)

        // Calculate the width of the regular and bold parts combined
        val totalWidthBeforeFrom = point9 + boldWidthFrom
        val totalWidthBetween = pointTwo9 + boldWidthTo

        // Draw the bold part "request.validFrom"
        canvas.drawText(request.validFrom!!, 20f + point9, position, boldPaint)

        // Draw the regular part " to "
        canvas.drawText(" to ", 20f + totalWidthBeforeFrom, position, paint)

        // Draw the bold part "request.validTo"
        canvas.drawText(
            request.validTo!!,
            20f + totalWidthBeforeFrom + pointTwo9,
            position,
            boldPaint
        )

        // Draw the regular part " (Valid upto 3 Hours) "
        canvas.drawText(
            " (Valid upto 3 Hours) ",
            20f + totalWidthBeforeFrom + totalWidthBetween,
            position,
            paint
        )


    }

    private fun showPdf(context: Activity, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("application/pdf")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.setPackage("com.whatsapp")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            context.startActivity(Intent.createChooser(intent, "Share PDF using"))
        } catch (ex: ActivityNotFoundException) {
            // Handle case where WhatsApp is not installed
            ex.printStackTrace()
        }
    }

    private fun generateQRCode(data: String, context: Activity): Bitmap? {
        val qrCodeColor = ContextCompat.getColor(context, R.color.qrCode)
        val hints: MutableMap<EncodeHintType, Any> =
            EnumMap(com.google.zxing.EncodeHintType::class.java)
        hints[EncodeHintType.MARGIN] = 0
        val writer = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 400, 400, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) qrCodeColor else Color.TRANSPARENT)
                }
            }
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    private fun openPdfFile(context: Context, file: File) {
        val pdfUri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(pdfUri, "application/pdf")
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show()
        }
    }

    fun getNextValue(addValue: Float): Float {
        currentValue += addValue
        return currentValue
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

}