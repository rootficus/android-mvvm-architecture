package com.fionpay.agent.ui.main.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fionpay.agent.data.model.response.GetModemsListResponse
import com.fionpay.agent.databinding.ModemBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ModemDetailScreenFragment : BottomSheetDialogFragment() {
    interface BottomDialogEvent {
        fun onAddRequest(getModemsListResponse: GetModemsListResponse, amount: Double)
    }

    var listener: BottomDialogEvent? = null
    private lateinit var binding: ModemBottomSheetBinding
    var currentBalance: Double? = 0.0
    var totalBalance: Double? = 0.0

    lateinit var getModemsListResponse: GetModemsListResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ModemBottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheetInternal!!).state =
                BottomSheetBehavior.STATE_EXPANDED
        }
        getModemsListResponse =
            arguments?.getSerializable(GetModemsListResponse::class.java.name) as GetModemsListResponse
        currentBalance = arguments?.getString("CurrentBalance")?.toDouble()
        binding.labelTotalBalance.text = "New balance will be: ৳${currentBalance}"
        binding.labelTitle.text =
            "${getModemsListResponse.firstName} ${getModemsListResponse.lastName}"
        binding.etCurrentBalance.setText("৳${getModemsListResponse.balance.toString()}")

        Log.i("Current:", "${binding.etCurrentBalance.text.toString()}")

        binding.etUpdateBalance.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    val newVal = s.toString().toDouble()
                    totalBalance = currentBalance?.minus(newVal)
                    if (totalBalance != null && totalBalance!! > 0.0) {
                        binding.labelTotalBalance.text = "New balance will be: ৳$totalBalance"
                    } else {
                        Toast.makeText(
                            context,
                            "Entered amount should be less than agent balance",
                            Toast.LENGTH_SHORT
                        ).show()
                        "New balance will be: ৳${currentBalance}"
                        binding.etUpdateBalance.setText("")
                    }

                } else {
                    binding.labelTotalBalance.text =
                        "New balance will be: ৳${currentBalance}"
                }

            }
        })

        binding.imageClose.setOnClickListener {
            dismiss()
        }
        binding.btnUpdate.setOnClickListener {
            if (binding.etUpdateBalance.text.toString().isEmpty()) {
                binding.etUpdateBalance.error = "Please enter amount"
            } else {

                val amount = binding.etUpdateBalance.text.toString().toDouble()
                listener?.onAddRequest(
                    getModemsListResponse,
                    amount
                )
                binding.etUpdateBalance.setText("")
            //AddModemBalanceModel(getModemsListResponse.id, amount)

            }
        }
    }

}