package com.fionpay.agent.ui.main.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fionpay.agent.data.model.response.GetModemsListResponse
import com.fionpay.agent.databinding.ModemBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ModemDetailScreenFragment : BottomSheetDialogFragment() {
    interface BottomDialogEvent {
        fun onAddRequest(getModemsListResponse: GetModemsListResponse, amount: Double)
        fun onRemoveRequest(getModemsListResponse: GetModemsListResponse, amount: Double)
    }

    var listener: BottomDialogEvent? = null
    private lateinit var binding: ModemBottomSheetBinding
    private var balanceStatus = ""
    var currentBalance: Double? = 0.0
    var totalBalance: Double? = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ModemBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getModemsListResponse =
            arguments?.getSerializable(GetModemsListResponse::class.java.name) as GetModemsListResponse
        currentBalance = arguments?.getString("CurrentBalance")?.toDouble()
        binding.labelTotalBalance.text = "New balance will be: ৳${currentBalance}"
        binding.labelTitle.text =
            "${getModemsListResponse.firstName} ${getModemsListResponse.lastName}"
        binding.etCurrentBalance.setText("৳${getModemsListResponse.balance}")

        binding.etUpdateBalance.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val lastName = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    val newVal = s.toString().toDouble()
                    totalBalance = currentBalance?.minus(newVal)
                    if (totalBalance != null && totalBalance!! > 0.0) {
                        binding.labelTotalBalance.text = "New balance will be: ৳$totalBalance"
                    } else {
                        Toast.makeText(context, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                        "New balance will be: ৳${currentBalance}"
                        binding.etUpdateBalance.setText("")
                    }

                } else {
                    binding.labelTotalBalance.text =
                        "New balance will be: ৳${currentBalance}"
                }

            }
        })
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.btnAdd.id -> {
                    balanceStatus = "Add"
                }

                binding.btnRemove.id -> {
                    balanceStatus = "Remove"
                }
            }
        }
        binding.imageClose.setOnClickListener {
            dismiss()
        }
        binding.btnUpdate.setOnClickListener {
            if (balanceStatus.isEmpty()) {
                Toast.makeText(context, "Please choose balance type ", Toast.LENGTH_SHORT).show()
            } else if (binding.etUpdateBalance.text.toString().isEmpty()) {
                binding.etUpdateBalance.error = "Please enter amount"
            } else {
                val amount = binding.etUpdateBalance.text.toString().toDouble()
                if (balanceStatus == "Add") {
                    listener?.onAddRequest(getModemsListResponse, amount)   //AddModemBalanceModel(getModemsListResponse.id, amount)
                } else if (balanceStatus == "Remove") {
                    listener?.onRemoveRequest(
                      getModemsListResponse, amount
                    )     //  AddModemBalanceModel( getModemsListResponse.id, amount )
                }
            }
        }
    }
}