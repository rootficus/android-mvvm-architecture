package com.fionpay.agent.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fionpay.agent.data.model.response.GetModemsListResponse
import com.fionpay.agent.databinding.ModemBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ModemDetailScreenFragment : BottomSheetDialogFragment() {
    interface BottomDialogEvent {
        fun onAcceptRequest(getModemsListResponse: GetModemsListResponse)
        fun onRejectedRequest(getModemsListResponse: GetModemsListResponse)
    }

    var listener: BottomDialogEvent? = null
    private lateinit var binding: ModemBottomSheetBinding

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
        val totalBalance = arguments?.getString("TotalBalance")
        binding.labelTitle.text =
            "${getModemsListResponse.firstName} ${getModemsListResponse.lastName}"
        binding.etCurrentBalance.setText("৳${getModemsListResponse.balance}")
        val selectedId: Int = binding.radioGroup.checkedRadioButtonId

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.btnAdd.id -> {
                    Toast.makeText(context, "Add", Toast.LENGTH_SHORT).show()
                }

                binding.btnRemove.id -> {
                    Toast.makeText(context, "Remove", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.imageClose.setOnClickListener {
            dismiss()
        }
    }
}