package com.rf.geolgy.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_DEFAULT
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.URLSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Akash.Singh
 * RootFicus.
 */
fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

/**
 * Extention Toast
 */
fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


/**
 * Fragment Transaction
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.showBottomSheet(fragment: BottomSheetDialogFragment) {
    fragment.showNow(supportFragmentManager, fragment.tag)
}

fun AppCompatActivity.hideBottomSheet(fragment: BottomSheetDialogFragment) {
    fragment.dismiss()
}

fun AppCompatActivity.addFragments(fragment: Fragment, frameId: Int, addToStack: Boolean? = false) {
    supportFragmentManager.inTransaction {
        if (addToStack == true) {
            add(frameId, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
        } else {
            add(frameId, fragment, fragment.javaClass.simpleName)
        }
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction {
        replace(
            frameId,
            fragment,
            fragment.javaClass.simpleName
        )
    }
}

fun Fragment.addChildFragment(fragment: Fragment, frameId: Int, addToStack: Boolean? = false) {
    childFragmentManager.inTransaction {
        if (addToStack == true) {
            add(frameId, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
        } else {
            add(frameId, fragment, fragment.javaClass.simpleName)
        }
    }
}

fun Fragment.replaceChildFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.inTransaction { replace(frameId, fragment, fragment.javaClass.simpleName) }
}

fun AppCompatActivity.isFragmentAvailableInStack(fragment: Fragment): Boolean {
    return supportFragmentManager.findFragmentByTag(fragment.getTagName()) == null
}


fun AppCompatTextView.setHtmlToTextView(html: String) {
    var sequence: CharSequence = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    sequence = sequence.trim()
    val strSpannableString = SpannableStringBuilder(sequence)
    var urlSpan: Array<URLSpan> =
        strSpannableString.getSpans(0, sequence.length, URLSpan::class.java)
    /*urlSpan.forEach {
        makeLinkClickable(strSpannableString, it, true, txtView.context)
    }*/
    this.text = strSpannableString
    //txtView.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

/**
 * single pop in container
 */
fun AppCompatActivity.popBackStack() {
    hideSoftKeyboard()
    supportFragmentManager.popBackStack()
}

/**
 * pop Back Stack Inclusive in container
 */
fun AppCompatActivity.popBackStackInclusive() {
    hideSoftKeyboard()
    if (supportFragmentManager.backStackEntryCount > 0)
        supportFragmentManager.popBackStack(
            supportFragmentManager.getBackStackEntryAt(0).id,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
}

/**
 * get Current Fragment
 */
fun AppCompatActivity.getCurrentFragment(): Fragment? {
    val fragmentManager = supportFragmentManager
    var fragmentTag: String? = ""

    if (fragmentManager.backStackEntryCount > 0)
        fragmentTag =
            fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1).name

    return fragmentManager.findFragmentByTag(fragmentTag)
}


/**
 * Edit text listener
 */
fun AppCompatEditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cb(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

/**
 * Seekbar listener
 */
fun SeekBar.onChangeSeekbar(progress: (Int) -> Unit) {
    this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progressCount: Int, fromUser: Boolean) {
            progress(progressCount)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    })
}


/**
 * request focus
 */
fun AppCompatActivity.requestFocus(view: View) {
    if (view.requestFocus()) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}

/**
 * Extension method to provide show keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

/**
 * Extension method to provide hide keyboard for [Activity].
 */
fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

/**
 * Extension method to provide hide keyboard for [Fragment].
 */
fun Fragment.hideSoftKeyboard() {
    activity?.hideSoftKeyboard()
}

/**
 * Extension method to provide hide keyboard for [View].
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * show view
 */
fun View.show() {
    this.visibility = View.VISIBLE
}

/**
 * hide view
 */
fun View.hide() {
    this.visibility = View.GONE
}

/**
 * invisible view
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * check view is visible
 */
fun View.isViewVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun Any.getTagName(): String {
    return this::class.java.simpleName
}

/**
 * Open Permission Screen
 */
fun Context.openPermissionSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:$packageName")
    ).apply {
        addCategory(CATEGORY_DEFAULT)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(this)
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}


/**
 * Retrofit
 */
fun <T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
    val callBackKt = CallBackKt<T>()
    callback.invoke(callBackKt)
    this.enqueue(callBackKt)
}

/**
 * Create Plain requestBody
 */
fun String.createPlainRequestBody(): RequestBody =
    RequestBody.create("text/plain".toMediaTypeOrNull(), this)


/***
 * Mask account number
 */
fun String.showLast4DigitWithMask(): String {
    if (this.length > 4) {
        val maskLength = this.length - 4
        StringBuilder().apply {
            for (i in 0..maskLength) {
                append('X')
            }
        }.also {
            return it.toString().plus(this.takeLast(4))
        }
    } else {
        return this
    }
}

/**
 * get commaSeparatorAmount from server
 */

fun String.addCommaSeparatorAmount(): String {
    val actualAmount = this.substringBefore(".")
    var decimalAmount = this.substringAfterLast(".")
    when (decimalAmount.length) {
        0 -> {
            decimalAmount = decimalAmount.plus("00")
        }

        1 -> {
            decimalAmount = decimalAmount.plus("0")
        }
    }
    if (this.isNotEmpty()) {
        return if (actualAmount.length <= 3) {
            if (this.contains(".")) {
                actualAmount.plus(".").plus(decimalAmount)
            } else {
                actualAmount.plus(".00")
            }
        } else {
            val formatter = actualAmount.takeLast(3)
            if (this.contains(".")) {
                DecimalFormat("##,##,##").format(
                    BigDecimal(
                        actualAmount.substring(
                            0,
                            actualAmount.length - 3
                        )
                    )
                ).plus(",").plus(formatter).plus(".").plus(decimalAmount)
            } else {
                DecimalFormat("##,##,##").format(
                    BigDecimal(
                        actualAmount.substring(
                            0,
                            actualAmount.length - 3
                        )
                    )
                ).plus(",").plus(formatter).plus(".00")
            }
        }
    } else {
        return this
    }
}


fun AppCompatImageView.rotateNiddle(angle: Float) {

    val animation = RotateAnimation(
        0.toFloat(),
        angle,
        RotateAnimation.RELATIVE_TO_SELF,
        1f,
        RotateAnimation.RELATIVE_TO_SELF,
        1f
    )

    animation.duration = 1000
    //  imageView
    this.animation = animation
    animation.fillAfter = true
}

fun AppCompatTextView.leftDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}

//Round the decimal value
fun roundDecimal(number: Double): Double {
    return DecimalFormat("#.##").format(number).toDouble()
}

//round decimal for string
fun roundDecimalWithoutPercent(number: String): String {
    number?.let {
        try {
            val d = it.toDouble()
            return DecimalFormat("#.##").format(d).toDouble().toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return ""
}

//round decimal for string
fun roundDecimal(number: String): String {
    number?.let {
        try {
            val d = it.toDouble()
            return DecimalFormat("#.##").format(d).toDouble().toString().plus("%")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return ""
}

fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).toInt()


//To tittle case
fun toTitleCase(str: String?): String? {
    if (str == null) {
        return null
    }
    var space = true
    val builder = java.lang.StringBuilder(str)
    val len = builder.length
    for (i in 0 until len) {
        val c = builder[i]
        if (space) {
            if (!Character.isWhitespace(c)) {
                // Convert to title case and switch out of whitespace mode.
                builder.setCharAt(i, Character.toTitleCase(c))
                space = false
            }
        } else if (Character.isWhitespace(c)) {
            space = true
        } else {
            builder.setCharAt(i, Character.toLowerCase(c))
        }
    }
    return builder.toString()


}




