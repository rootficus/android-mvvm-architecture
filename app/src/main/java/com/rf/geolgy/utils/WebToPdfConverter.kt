import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Environment
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object WebToPdfConverter {
    private const val TAG = "WebToPdfConverter"
    fun convertToPdf(webView: WebView, url: String?) {
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                createPdf(webView)
            }
        }
        webView.loadUrl(url!!)
    }

    private fun createPdf(webView: WebView) {
        val document = PdfDocument()
        val webViewWidth = webView.width
        val webViewHeight = webView.contentHeight
        if (webViewWidth > 0 && webViewHeight > 0) {
            val pageInfo = PageInfo.Builder(webViewWidth, webViewHeight, 1).create()
            val page = document.startPage(pageInfo)
            webView.draw(page.canvas)
            document.finishPage(page)
            val fileName = "webpage_pdf.pdf"
            val file = File(Environment.getExternalStorageDirectory(), fileName)
            try {
                document.writeTo(FileOutputStream(file))
                Toast.makeText(webView.context, "PDF saved to internal storage", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: IOException) {
                Log.e(TAG, "Error creating PDF: " + e.message)
                Toast.makeText(webView.context, "Error saving PDF", Toast.LENGTH_SHORT).show()
            }
            document.close()
        } else {
            Toast.makeText(
                webView.context,
                "WebView width or height is invalid",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
