package com.rf.macgyver.ui.main.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.rf.macgyver.R
import com.rf.macgyver.databinding.ActivityCalculatorUiBinding
import com.rf.macgyver.ui.base.BaseActivity
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorActivity : BaseActivity<ActivityCalculatorUiBinding, Any?>(R.layout.activity_calculator_ui) {

    private lateinit var editText: TextView

    private lateinit var viewBinding : ActivityCalculatorUiBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCalculatorUiBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_calculator_ui)

        editText = findViewById(R.id.edittext)

        val buttons = intArrayOf(
            R.id.zeroId,
            R.id.oneBtnId,
            R.id.twoBtnId,
            R.id.threeBtnId,
            R.id.fourBtnId,
            R.id.fiveBtnId,
            R.id.sixBtnId,
            R.id.sevenBtnId,
            R.id.eightBtnId,
            R.id.nineBtnId,
            R.id.decimalId,
            R.id.plusBtnId,
            R.id.minusBtnId,
            R.id.multiplyBtnId,
            R.id.divideBtnId,
            R.id.CBtnId,
            R.id.equalSignId,
            R.id.bracketsBtnId,
            R.id.doubleZeroId
        )
        for (buttonId in buttons) {
            findViewById<TextView>(buttonId).setOnClickListener(onClickListener)
        }
        val delBtn = findViewById<ImageView>(R.id.deleteIconId)
        delBtn.setOnClickListener {
            var str: String = editText.text.toString()
            if (str != "") {
                str = str.substring(0, str.length - 1)
                editText.setText(str)
            }
        }
    }


    private val onClickListener = View.OnClickListener { v ->
        val button = v as TextView
        when(val buttonText = button.text.toString()) {
            "C" -> editText.setText("")
            "( )" -> {val newTxt ="("+ editText.text.toString()+")"
                                     editText.setText(newTxt)}
            "=" -> calculateResult()

            else -> editText.append(buttonText)
        }
    }

    private fun calculateResult() {
        val expression = editText.text.toString()
        try {
            val result = ExpressionBuilder(expression).build().evaluate()
            editText.setText(result.toString())
        } catch (e: Exception) {
            editText.setText("Error")
        }
    }
}


