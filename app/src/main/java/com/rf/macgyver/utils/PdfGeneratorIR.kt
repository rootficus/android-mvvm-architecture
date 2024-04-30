import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.action.PdfAction
import com.itextpdf.kernel.pdf.annot.PdfAnnotation
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import com.rf.macgyver.R
import com.rf.macgyver.data.model.request.dailyReportData.Step1DrData
import com.rf.macgyver.data.model.request.dailyReportData.Step2DrData
import com.rf.macgyver.roomDB.model.IncidentReport
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.EnumMap

object PdfGeneratorIR {

    private var currentValue: Float = 0f
    private var date: String? = ""
    private var time: String? = ""
    private var incidentNo: String? = ""
    private var incidentArea: String? = ""
    private var incidentLoc: String? = ""
    private var incidentCause: String? = ""
    private var incidentSeverity: String? = ""
    private var vehicleNo: String? = ""
    private var vehicleName: String? = ""
    private var operatorName : String? = ""
    private var typeOfIncident : ArrayList<String> = arrayListOf()
    private var weatherCondition : String? = ""
    private var vehicleActivity : String? = ""
    private var damagesList : String? = ""
    private var additionalComment :String? = ""

    fun createIRPdf(
        context: Activity,
        entity : IncidentReport
    ) {
        try {
            this.date = entity.incidentDate
            this.time = entity.incidentTime
            this.incidentNo = entity.incidentNo
            this.incidentArea = entity.incidentArea.toString()
            this.incidentLoc = entity.incidentLocation
            this.incidentCause = entity.incidentCause
            this.incidentSeverity = entity.incidentSeverity
            this.vehicleNo = entity.vehicleNo
            this.vehicleName = entity.vehicleName
            this.operatorName = entity.operatorName
            this.typeOfIncident = entity.typeOfIncident
            this.weatherCondition = entity.weatherCondition
            this.vehicleActivity = entity.vehicleActivity
            this.damagesList = entity.damagesList
            this.additionalComment = entity.additionalComment

            val file = File(
                context.getExternalFilesDir("PDF"),
                "daily-report-${entity.incidentNo}.pdf"
            )
            val writer = PdfWriter(file)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument)
            document.setMargins(0f, 0f, 0f, 0f)
            // Create a new page
            val newPage = pdfDocument.addNewPage()
            val canvas = PdfCanvas(pdfDocument.firstPage)

            val rect = Rectangle(36f, 680f, 200f, 16f)

            val font = PdfFontFactory.createFont()
//            val boldFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)

            //        val watermarkText = "https://geologymining.jk.gov.in/  https://geologymining.jk.gov.in/"
            val link = PdfLinkAnnotation(rect)
                .setAction(PdfAction.createURI("https://www.google.com"))
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)
                .setColor(ColorConstants.BLUE)


            val boldFont = PdfFontFactory.createFont("Helvetica-Bold")

            // Add the link annotation to the new page
            newPage.addAnnotation(link)
            // Add watermark to each page
            // Calculate the number of repetitions needed vertically
            val lineHeight = 12f // Adjust as needed
            val pageHeight = PageSize.A4.height
            val pageWidth = PageSize.A4.width

            var currentPosition = pageHeight
            val colorInt = ContextCompat.getColor(context, R.color.lightBlue)// Adjust as needed
            val red = Color.red(colorInt)
            val green = Color.green(colorInt)
            val blue = Color.blue(colorInt)

            // Create Color object
            val textColor = DeviceRgb(red, green, blue)

            // Add watermark text repeatedly from top to bottom until the entire page is covered
            /*  while (currentPosition > 0) {
                val watermarkParagraph = Paragraph(watermarkText)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(textColor)
                    .setFontSize(12f)
                    .setMarginTop(10f)
                    .setMultipliedLeading(1.2f)
                    .setCharacterSpacing(1.5f)
                watermarkParagraph.setAction(PdfAction.createURI("https://geologymining.jk.gov.in/"))

                document.showTextAligned(
                    watermarkParagraph,
                    pageWidth / 2,
                    currentPosition - lineHeight / 2,
                    TextAlignment.CENTER
                )

                currentPosition -= lineHeight
            }
*/
            val dateLine = Paragraph(date)
                .setFont(font)
                .setFontSize(14f)
                .setMarginTop(15F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.9f)

            dateLine.setMarginBottom(0f)
            document.add(dateLine)

            val dayLine = Paragraph(time)
                .setFont(font)
                .setFontSize(12f)
                .setMarginTop(2F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.9f)

            dayLine.setMarginBottom(0f)
            document.add(dayLine)

            val reportNamePara = Paragraph(incidentNo)
                .setFont(boldFont)
                .setFontSize(14f)
                .setMarginTop(5F)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(0.9f)
            reportNamePara.setMarginBottom(0f)
            document.add(reportNamePara)

            val paragraph = Paragraph()
            paragraph.add(
                Paragraph(vehicleNo)
                    .setFont(boldFont)
                    .setFontSize(12f)
                    .setTextAlignment(TextAlignment.LEFT)
            )
            paragraph.add(
                Paragraph(vehicleName)
                    .setFont(font)
                    .setFontSize(12f)
                    .setTextAlignment(TextAlignment.RIGHT)
            )
            document.add(paragraph)

            val question2AnsPara = Paragraph(operatorName)
                .setFont(boldFont)
                .setFontSize(13f)
                .setMarginTop(0F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.9f)
            document.add(question2AnsPara)

            val question1notePara = Paragraph(incidentLoc)
                .setFont(font)
                .setFontSize(12f)
                .setMarginTop(5F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.9f)
            document.add(question1notePara)


            val question2Title = Paragraph(incidentArea)
                .setFont(font)
                .setFontSize(12f)
                .setMarginTop(5F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.9f)
            document.add(question2Title)



            for (i in 0..typeOfIncident.size){
            val question2notePara = Paragraph(typeOfIncident[i])
                .setFont(boldFont)
                .setFontSize(12f)
                .setMarginTop(5F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.9f)
            document.add(question2notePara)
            }


            val question3Title = Paragraph(incidentCause)
                .setFont(boldFont)
                .setFontSize(12f)
                .setMarginTop(5F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.9f)
            document.add(question3Title)

            val question3AnsPara = Paragraph(incidentSeverity)
                .setFont(boldFont)
                .setFontSize(12f)
                .setMarginTop(0F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.9f)
            document.add(question3AnsPara)

                val question3notePara = Paragraph(weatherCondition)
                    .setFont(boldFont)
                    .setFontSize(12f)
                    .setMarginTop(5F)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMultipliedLeading(0.9f)
                document.add(question3notePara)


            val question4Title = Paragraph(vehicleActivity)
                .setFont(boldFont)
                .setFontSize(12f)
                .setMarginTop(5F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.9f)
            document.add(question4Title)

            val question4AnsPara = Paragraph(damagesList)
                .setFont(boldFont)
                .setFontSize(12f)
                .setMarginTop(0F)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.9f)
            document.add(question4AnsPara)



            if (!additionalComment.isNullOrEmpty()) { // Check if note is not null or empty

                val question7notePara = Paragraph(additionalComment)
                    .setFont(boldFont)
                    .setFontSize(12f)
                    .setMarginTop(5F)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMultipliedLeading(0.9f)
                document.add(question7notePara)
            }




            // Add QR code image to PDF
             /*val qrCodeBitmap = generateQRCode("https://geolgyminingjk.in/challan/${entity?.reportName}", context)
            qrCodeBitmap?.let {
                val qrCodeImage = Image(ImageDataFactory.create(bitmapToByteArray(it)))
                qrCodeImage.scaleToFit(85f, 82f)
                document.add(qrCodeImage)
            }*/

            /*val qrCode = "(QR-Code)"
            val qrCodeParagraph = Paragraph(qrCode)
                .setFont(font)
                .setFontSize(8f)
                .setMarginLeft(14F)
                .setMarginTop(5f)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.6f)
            document.add(qrCodeParagraph)*/

            /*val validFromTo = "Validity from ${request.validFrom} to ${request.validTo}"
            val validFromToParagraph = Paragraph(validFromTo)
                .setFont(boldFont)
                .setFontSize(14f)
                .setMarginTop(18F)
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(0.6f)
            document.add(validFromToParagraph)

            createPoint1(document, step1DRFragment?.licenceType, font, boldFont)
            createPointTwo1(document, request, font, boldFont)*/
/*
            val point2 = "2. Name & Style of Concessionary ......................................."
            val point2Paragraph = Paragraph(point2)
                .setFont(font)
                .setFontSize(12f)
                .setMarginLeft(10F)
                .setMarginTop(8f)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.9f)
            document.add(point2Paragraph)
            val point3 = "3. Location of mineral concession area ................................."
            val point3Paragraph = Paragraph(point3)
                .setFont(font)
                .setFontSize(12f)
                .setMarginLeft(10F)
                .setMarginTop(8f)
                .setTextAlignment(TextAlignment.LEFT)
                .setMultipliedLeading(0.9f)
            document.add(point3Paragraph)
            val point4 =
                "4. Type of mineral Granted on mineral concessions ................................."
            val point4Paragraph = Paragraph(point4)
                .setFont(font)
                .setFontSize(12f)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginLeft(10F)
                .setMarginTop(6f)
                .setMultipliedLeading(0.9f)
            document.add(point4Paragraph)
            val point5 =
                "5. Quantity of mineral granted on mineral Concessions ................................."
            val point5Paragraph = Paragraph(point5)
                .setFont(font)
                .setFontSize(12f)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginLeft(10F)
                .setMarginTop(8f)
                .setMultipliedLeading(0.9f)
            document.add(point5Paragraph)*/

            /*createPoint6(document, "${request.nameAndLocation}", font, boldFont)

            createPoint7(document, request.product, font, boldFont)

            createPoint8(document, request.quantityDispatched, font, boldFont)

            createPoint9(document, request, font, boldFont)

            createPoint10(document, request, font, boldFont)
            createPoint11(document, request, font, boldFont)
            createPoint12(document, request, font, boldFont)

            createPoint13(document, request.vehicleNo, font, boldFont)
            createPoint14(document, request.nameAndAddress, font, boldFont)
            createPoint15(
                document,
                "${request.driverName} , ${request.driverPhone}",
                font,
                boldFont
            )

            val xPosition = 10f
            val yPosition = 20f // Adjust the Y position as needed

            val pointNote =
                "Note: The Information mentioned in e-Challan, Such as (Validity and Vehicle No.) should be matched with the"
            val pointNoteParagraph = Paragraph(pointNote)
                .setFont(boldFont)
                .setFontSize(11f)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginLeft(5F)
                .setMarginTop(3f)
                .setMultipliedLeading(1f)


            document.showTextAligned(
                pointNoteParagraph,
                xPosition,
                yPosition + 150f,
                TextAlignment.LEFT
            )
            val   pointNote2 =
                "information mentioned in the https://geologymining.jk.gov.in/ which can be seen after scanning the QR code"

            val pointNote2Paragraph = Paragraph(pointNote2)
                .setFont(boldFont)
                .setFontSize(11f)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginLeft(5F)
                .setMarginTop(3f)
                .setMultipliedLeading(1f)


            document.showTextAligned(
                pointNote2Paragraph,
                xPosition,
                yPosition + 137f,
                TextAlignment.LEFT
            )

            val pointNote3 = "encrypted on e-Challan."

            val pointNote3Paragraph = Paragraph(pointNote3)
                .setFont(boldFont)
                .setFontSize(11f)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginLeft(5F)
                .setMarginTop(3f)
                .setMultipliedLeading(1f)


            document.showTextAligned(
                pointNote3Paragraph,
                xPosition,
                yPosition + 124f,
                TextAlignment.LEFT
            )


            val selfApproved = "Self Approved by Mineral Concessionary"
            val selfApprovedParagraph = Paragraph(selfApproved)
                .setFont(font)
                .setFontSize(10f)
                .setTextAlignment(TextAlignment.LEFT)

            val bottomMargin = 100f // Adjust the bottom margin as needed

            document.showTextAligned(
                selfApprovedParagraph,
                30F,
                yPosition + bottomMargin,
                TextAlignment.LEFT
            )
            val signature =
                "Signature & Seal of Mineral Concessionary"
            val rect2Width = pageWidth / 2
            val signatureParagraph = Paragraph(signature)
                .setFont(font)
                .setFontSize(10f)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginTop(-20f)
                .setMultipliedLeading(0.9f)

            document.showTextAligned(
                signatureParagraph,
                rect2Width.plus(25f),
                yPosition + bottomMargin,
                TextAlignment.LEFT
            )

            val drawableId: Int =
                R.drawable.approved // Replace your_image with your drawable resource ID

            // Convert drawable to Bitmap
            val bitmap: Bitmap = drawableToBitmap(context, drawableId)
            // Create an Image object from the Bitmap
            val image = Image(ImageDataFactory.create(bitmapToByteArray(bitmap)))

// Set position and size of the image
            image.setFixedPosition(30f, 30f) // Adjust position as needed
            image.scaleToFit(95f, 95f) // Adjust width and height as needed

// Add the image to the document
            document.add(image)


            canvas.setFillColor(ColorConstants.WHITE)

            // Draw the transparent rectangle
            val rectWidth = pageWidth / 2 - 35 // Adjust as needed
            val rectHeight = 120f // Adjust as needed
            val rect1 = Rectangle(20f, 20f, rectWidth, rectHeight)
            canvas.rectangle(rect1)
            canvas.stroke()

            // Repeat for the second rectangle
            val rect2 = Rectangle(pageWidth / 2 + 10, 20f, rectWidth, rectHeight)
            canvas.rectangle(rect2)
            canvas.stroke()

             */



            try {
                //document.close()
                 openPdfFile(context, file)
                //showPdf(context, file)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("Error", "${e.message}")
            // Handle the exception (e.g., log an error message)
        }

    }
/*
    private fun createPoint12(
        document: Document,
        request: Step2DrData,
        font: PdfFont?,
        boldFont: PdfFont?
    ) {
        val gstNumber = Text(request.gstNumber).setFont(boldFont)
        val qualityPercentage = Text(qualityPercentage).setFont(boldFont)
        val qualityAmount = Text(qualityAmount).setFont(boldFont)

        val pointTwo1 = "12. GST Bill/No. "
        val pointTwo1Paragraph = Paragraph(pointTwo1)
            .setFont(font)
            .setFontSize(12f)
            .setMarginLeft(10F)
            .setMarginTop(8f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMultipliedLeading(0.9f)

        pointTwo1Paragraph.add(gstNumber) // Add validFrom chunk
        pointTwo1Paragraph.add(" Quantity ")
        pointTwo1Paragraph.add(qualityPercentage)
        pointTwo1Paragraph.add("% Amount Rs.")
        pointTwo1Paragraph.add(qualityAmount)
        pointTwo1Paragraph.add(" (Enclose copy of GST Invoice)")

        document.add(pointTwo1Paragraph)
    }

    private fun createPoint11(
        document: Document,
        request: Step2DrData,
        font: PdfFont?,
        boldFont: PdfFont?
    ) {
        val rateOfMineral = Text(rateOfMineral).setFont(boldFont)
        val rateOfMineralTotal = Text(rateOfMineralTotal).setFont(boldFont)

        val pointTwo1 = "11. Rate of Mineral Rs."
        val pointTwo1Paragraph = Paragraph(pointTwo1)
            .setFont(font)
            .setFontSize(12f)
            .setMarginLeft(10F)
            .setMarginTop(8f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMultipliedLeading(0.9f)

        pointTwo1Paragraph.add(rateOfMineral) // Add validFrom chunk
        pointTwo1Paragraph.add(" Total Amount (Excluding GST and Transportation charges) Rs.")
        pointTwo1Paragraph.add(rateOfMineralTotal) // Add validTo chunk

        document.add(pointTwo1Paragraph)
    }

    private fun createPoint10(
        document: Document,
        request: Step2DrData,
        font: PdfFont?,
        boldFont: PdfFont?
    ) {
        val validFromText = Text(request.questionData?.title).setFont(boldFont)
        val validToText = Text(request.questionData?.selectedAnswer ).setFont(boldFont)

        val pointTwo1 = "10. Route of the Transportation- Source "
        val pointTwo1Paragraph = Paragraph(pointTwo1)
            .setFont(font)
            .setFontSize(12f)
            .setMarginLeft(10F)
            .setMarginTop(8f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMultipliedLeading(0.9f)

        pointTwo1Paragraph.add(validFromText) // Add validFrom chunk
        pointTwo1Paragraph.add(" Destination ")
        pointTwo1Paragraph.add(validToText) // Add validTo chunk

        document.add(pointTwo1Paragraph)
    }

    private fun createPoint9(
        document: Document,
        request: Step2DrData,
        font: PdfFont?,
        boldFont: PdfFont?
    ) {
        val validFromText = Text(request.questionData?.title).setFont(boldFont)
        val validToText = Text(request.questionData?.selectedAnswer ).setFont(boldFont)

        val pointTwo1 = "9. DATE & TIME of dispatch  "
        val pointTwo1Paragraph = Paragraph(pointTwo1)
            .setFont(font)
            .setFontSize(12f)
            .setMarginLeft(10F)
            .setMarginTop(8f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMultipliedLeading(0.9f)

        pointTwo1Paragraph.add(validFromText) // Add validFrom chunk
        pointTwo1Paragraph.add(" to ")
        pointTwo1Paragraph.add(validToText) // Add validTo chunk
        document.add(pointTwo1Paragraph)
    }

    private fun createPoint6(
        document: Document,
        nameAndLocation: String,
        font: PdfFont?,
        boldFont: PdfFont?
    ) {
        val point1 = "6. Name & Location of Stone crusher Unit and Holder "
        val licenceTypeIndex = point1.length // Start index for the licence type


        val part1 = point1.substring(0, licenceTypeIndex)
        val part3 = point1.substring(licenceTypeIndex)

        val point7Paragraph = Paragraph()
            .setFont(font)
            .setFontSize(12f)
            .setMarginLeft(10F)
            .setMarginTop(8f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMultipliedLeading(0.9f)

        point7Paragraph.add(part1)
        point7Paragraph.add(Text(nameAndLocation).setFont(boldFont))
        point7Paragraph.add(part3)

        document.add(point7Paragraph)
    }

    private fun createPointTwo1(
        document: Document,
        request: Step2DrData,
        font: PdfFont?,
        boldFont: PdfFont?
    ) {
        val validFromText = Text(request.questionData?.title).setFont(boldFont)
        val validToText = Text(request.questionData?.selectedAnswer ).setFont(boldFont)

        val pointTwo1 = "Issuing date "
        val pointTwo1Paragraph = Paragraph(pointTwo1)
            .setFont(font)
            .setFontSize(12f)
            .setMarginLeft(10F)
            .setMarginTop(8f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMultipliedLeading(0.9f)

        pointTwo1Paragraph.add(validFromText) // Add validFrom chunk
        pointTwo1Paragraph.add(" Valid upto ")
        pointTwo1Paragraph.add(validToText) // Add validTo chunk

        document.add(pointTwo1Paragraph)

    }

    private fun createPoint15(
        document: Document,
        nameNumber: String,
        font: PdfFont?,
        boldFont: PdfFont?
    ) {
        val point1 = "15. Name & Phone No. of Driver "
        val licenceTypeIndex = point1.length // Start index for the licence type


        val part1 = point1.substring(0, licenceTypeIndex)
        val part3 = point1.substring(licenceTypeIndex)

        val point7Paragraph = Paragraph()
            .setFont(font)
            .setFontSize(12f)
            .setMarginLeft(10F)
            .setMarginTop(8f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMultipliedLeading(0.9f)

        point7Paragraph.add(part1)
        point7Paragraph.add(Text(nameNumber).setFont(boldFont))
        point7Paragraph.add(part3)

        document.add(point7Paragraph)

    }

    private fun createPoint14(
        document: Document,
        nameAndAddress: String?,
        font: PdfFont?,
        boldFont: PdfFont?
    ) {
        val point1 = "14. Name & Address of Consignee / Buyer / Purchase "
        val licenceTypeIndex = point1.length // Start index for the licence type


        val part1 = point1.substring(0, licenceTypeIndex)
        val part3 = point1.substring(licenceTypeIndex)

        val point7Paragraph = Paragraph()
            .setFont(font)
            .setFontSize(12f)
            .setMarginLeft(10F)
            .setMarginTop(8f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMultipliedLeading(0.9f)

        point7Paragraph.add(part1)
        point7Paragraph.add(Text(nameAndAddress).setFont(boldFont))
        point7Paragraph.add(part3)

        document.add(point7Paragraph)
    }

    private fun createPoint13(
        document: Document,
        vehicleNo: String?,
        font: PdfFont?,
        boldFont: PdfFont?
    ) {
        val point1 = "13. Vehicle No. "
        val licenceTypeIndex = point1.length // Start index for the licence type


        val part1 = point1.substring(0, licenceTypeIndex)
        val part3 = point1.substring(licenceTypeIndex)

        val point7Paragraph = Paragraph()
            .setFont(font)
            .setFontSize(12f)
            .setMarginLeft(10F)
            .setMarginTop(8f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMultipliedLeading(0.9f)

        point7Paragraph.add(part1)
        point7Paragraph.add(Text(vehicleNo).setFont(boldFont))
        point7Paragraph.add(part3)

        document.add(point7Paragraph)
    }

    private fun createPoint8(
        document: Document,
        quantityDispatched: String?,
        font: PdfFont?,
        boldFont: PdfFont?
    ) {
        val point1 = "8. Quantity of mineral dispatched "
        val licenceTypeIndex = point1.length // Start index for the licence type


        val part1 = point1.substring(0, licenceTypeIndex)
        val part3 = point1.substring(licenceTypeIndex)

        val point7Paragraph = Paragraph()
            .setFont(font)
            .setFontSize(12f)
            .setMarginLeft(10F)
            .setMarginTop(8f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMultipliedLeading(0.9f)

        point7Paragraph.add(part1)
        point7Paragraph.add(Text(quantityDispatched).setFont(boldFont))
        point7Paragraph.add(part3)

        document.add(point7Paragraph)
    }

    private fun createPoint7(
        document: Document,
        product: String?,
        font: PdfFont?,
        boldFont: PdfFont?
    ) {
        val point1 = "7. Type of Finished Products: "
        val licenceTypeIndex = point1.length // Start index for the licence type


        val part1 = point1.substring(0, licenceTypeIndex)
        val part3 = point1.substring(licenceTypeIndex)

        val point7Paragraph = Paragraph()
            .setFont(font)
            .setFontSize(12f)
            .setMarginLeft(10F)
            .setMarginTop(8f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMultipliedLeading(0.9f)

        point7Paragraph.add(part1)
        point7Paragraph.add(Text(product).setFont(boldFont))
        point7Paragraph.add(part3)

        document.add(point7Paragraph)
    }

    private fun createPoint1(
        document: Document,
        licenceType: String?,
        font: PdfFont,
        boldFont: PdfFont
    ) {
        val point1 = "1. Type of mineral concessions Lease / License / Permit no. "
        val licenceTypeIndex = point1.length // Start index for the licence type


        val part1 = point1.substring(0, licenceTypeIndex)
        val part3 = point1.substring(licenceTypeIndex)

        val point1Paragraph = Paragraph()
            .setFont(font)
            .setFontSize(12f)
            .setMarginLeft(10F)
            .setMarginTop(10f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMultipliedLeading(0.9f)

        point1Paragraph.add(part1)
        point1Paragraph.add(Text(licenceType).setFont(boldFont))
        point1Paragraph.add(part3)

        document.add(point1Paragraph)
    }*/

            fun openPdfFile(context: Context, file: File) {
                val pdfUri = FileProvider.getUriForFile(
                    context,
                    "${context.applicationContext.packageName}.fileprovider",
                    file
                )
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(pdfUri, "application/pdf")
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

            // Function to convert drawable resource to Bitmap
            fun drawableToBitmap(context: Context, drawableId: Int): Bitmap {
                return BitmapFactory.decodeResource(context.resources, drawableId)
            }

            fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                return stream.toByteArray()
            }

            private fun generateQRCode(data: String, context: Activity): Bitmap? {
                val qrCodeColor = ContextCompat.getColor(context, R.color.lightBlue)
                val hints: MutableMap<EncodeHintType, Any> =
                    EnumMap(EncodeHintType::class.java)
                hints[EncodeHintType.MARGIN] = 0
                val writer = QRCodeWriter()
                try {
                    val bitMatrix: BitMatrix =
                        writer.encode(data, BarcodeFormat.QR_CODE, 60, 60, hints)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            bitmap.setPixel(
                                x,
                                y,
                                if (bitMatrix[x, y]) qrCodeColor else Color.TRANSPARENT
                            )
                        }
                    }
                    return bitmap
                } catch (e: WriterException) {
                    e.printStackTrace()
                }
                return null
            }


            private fun showPdf(context: Activity, file: File) {
                val uri =
                    FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

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

            // Rest of the functions like generateQRCode can be reused as they are
        }







