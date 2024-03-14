import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.action.PdfAction
import com.itextpdf.kernel.pdf.annot.PdfAnnotation
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Canvas
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.VerticalAlignment
import com.rf.geolgy.R
import com.rf.geolgy.data.model.request.CreateChallanRequest
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.EnumMap

object PdfGenerator2 {

    private var currentValue: Float = 0f

    fun createPdf(context: Activity, request: CreateChallanRequest, licenceType: String?) {
        try {
            val file = File(context.getExternalFilesDir("PDF"), "example.pdf")
            val writer = PdfWriter(file)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument)
            document.setMargins(0f, 0f, 0f, 0f)
            val paragraph = Paragraph()
            // Create a new page
            val newPage = pdfDocument.addNewPage()

            val rect = Rectangle(36f, 700f, 200f, 16f)

            val boldFont = com.itextpdf.kernel.font.PdfFontFactory.createFont()

            val watermarkText = "https://geologyminingjk.gov.in/ https://geologyminingjk.gov.in/"
            val watermark = Paragraph(watermarkText).setFontColor(ColorConstants.LIGHT_GRAY).setFontSize(12f)
            //watermark.setFixedPosition(36f, 700f)
            val link = PdfLinkAnnotation(rect)
                .setAction(PdfAction.createURI("https://www.google.com"))
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)
                .setColor(ColorConstants.BLUE)

            // Add the link annotation to the new page
            newPage.addAnnotation(link)
            // Add watermark to each page
            // Calculate the number of repetitions needed vertically
            val lineHeight = 12f // Adjust as needed
            val pageHeight = PageSize.A4.height
            val pageWidth = PageSize.A4.width

            var currentPosition = pageHeight
            val backgroundColor = DeviceRgb(255, 255, 255) // Adjust as needed
            val colorInt = ContextCompat.getColor(context, R.color.water_mark)// Adjust as needed
            val red = Color.red(colorInt)
            val green = Color.green(colorInt)
            val blue = Color.blue(colorInt)

// Create Color object
            val textColor = DeviceRgb(red, green, blue)
            // Add watermark text repeatedly from top to bottom until the entire page is covered
            while (currentPosition > 0) {
                val watermarkParagraph = Paragraph(watermarkText)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(textColor)
                    .setFontSize(8f)
                watermarkParagraph.setAction(PdfAction.createURI("https://www.google.com"))

                document.showTextAligned(
                    watermarkParagraph,
                    pageWidth / 2,
                    currentPosition - lineHeight / 2,
                    TextAlignment.CENTER
                )

                currentPosition -= lineHeight
            }




            //document.add(watermark)


            val govtText = "Government of Jammu Kashmir"
            val govtParagraph = Paragraph(govtText)
                .setFont(boldFont)
                .setFontSize( 10f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading( 0.6f)

            govtParagraph.setMarginBottom(0f)
            document.add(govtParagraph)

            val departmentOf = "Department of"
            val departmentParagraph = Paragraph(departmentOf)
                .setFont(boldFont)
                .setFontSize( 10f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(  0.6f)

            departmentParagraph.setMarginBottom(0f)
            document.add(departmentParagraph)

            val form = "Form"
            val formParagraph = Paragraph(form)
                .setFont(boldFont)
                .setFontSize( 10f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(  0.6f)
            formParagraph.setMarginBottom(0f)
            document.add(formParagraph)

            val seeRule = "[See Rule 38(5), 50(12), 60(1)(v), 70, 71]"

            val seeRuleParagraph = Paragraph(seeRule)
                .setFont(boldFont)
                .setFontSize( 10f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(  0.6f)
            document.add(seeRuleParagraph)

            val seeRule2 = "of challan for dispatch of mineral and its products"

            val seeRule2Paragraph = Paragraph(seeRule2)
                .setFont(boldFont)
                .setFontSize( 10f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(  0.6f)
            document.add(seeRule2Paragraph)

            val eChallan = "E-Challan"

            val eChallanParagraph = Paragraph(eChallan)
                .setFont(boldFont)
                .setFontSize( 10f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(  0.6f)
            document.add(eChallanParagraph)

            val challanNumber = "Challan No. JK08Y-${request.challanNumber}"

            val challanParagraph = Paragraph(challanNumber)
                .setFont(boldFont)
                .setFontSize( 10f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(  0.6f)
            document.add(challanParagraph)

            // Add QR code image to PDF
            val qrCodeBitmap = generateQRCode("qrText", context)
            qrCodeBitmap?.let {
                val qrCodeImage = Image(ImageDataFactory.create(bitmapToByteArray(it)))
                document.add(qrCodeImage)
            }

            val validFromTo = "Validity from ${request.validFrom} to ${request.validTo}"
            val validFromToParagraph = Paragraph(validFromTo)
                .setFont(boldFont)
                .setFontSize( 10f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(  0.6f)
            document.add(validFromToParagraph)

            val point1 = "1. Type of mineral concessions Lease / License / Permit no. $licenceType"
            val point1Paragraph = Paragraph(point1)
                .setFont(boldFont)
                .setFontSize( 10f)
                .setMarginLeft(10F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(  0.6f)
            document.add(point1Paragraph)
            val point2= "2. Name & Style of Concessionary ......................................."
            val point2Paragraph = Paragraph(point2)
                .setFont(boldFont)
                .setFontSize( 10f)
                .setMarginLeft(10F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(  0.6f)
            document.add(point2Paragraph)
            val point3= "3. Location of mineral concession area ................................."
            val point3Paragraph = Paragraph(point3)
                .setFont(boldFont)
                .setFontSize( 10f)
                .setMarginLeft(10F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(  0.6f)
            document.add(point3Paragraph)
            val point4= "4. Type of mineral Granted on mineral concessions ................................."
            val point4Paragraph = Paragraph(point4)
                .setFont(boldFont)
                .setFontSize( 10f)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginLeft(10F)
                .setMultipliedLeading(  0.6f)
            document.add(point4Paragraph)
            // Add other content here using Paragraph and other iText elements

            try {
                document.close()
                openPdfFile(context, file)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("Error", "${e.message}")
            // Handle the exception (e.g., log an error message)
        }

    }

    private fun openPdfFile(context: Context, file: File) {
        val pdfUri = FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(pdfUri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
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

    private fun generateQRCode(data: String, context: Activity): Bitmap? {
        val qrCodeColor = ContextCompat.getColor(context, R.color.qrCode)
        val hints: MutableMap<EncodeHintType, Any> =
            EnumMap(EncodeHintType::class.java)
        hints[EncodeHintType.MARGIN] = 0
        val writer = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 60, 60, hints)
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

    // Rest of the functions like generateQRCode can be reused as they are
}



