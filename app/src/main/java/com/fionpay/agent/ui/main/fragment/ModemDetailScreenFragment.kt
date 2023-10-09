package com.fionpay.agent.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.fionpay.agent.data.model.response.GetMessageManageRecord
import com.fionpay.agent.data.model.response.GetModemsListResponse
import com.fionpay.agent.databinding.ModemBottomSheetBinding
import org.jetbrains.annotations.Nullable


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
            arguments?.getSerializable(GetMessageManageRecord::class.java.name) as GetModemsListResponse
   /*     binding.textType?.text = getModemsListResponse.smsType.toString()
        binding.textOperator?.text = getModemsListResponse.operator.toString()
        binding.textDeviceId?.text = getModemsListResponse.deviceId.toString()
        binding.textSimId?.text = getModemsListResponse.simId.toString()
        binding.textDeviceInfo?.text = getModemsListResponse.deviceInfo.toString()
        //binding.textAgent?.text =getModemsListResponse.a.toString()
        if (getModemsListResponse.isActive == true) {
            binding.textStatus?.text = "Active"
        }else
        {
            binding.textStatus?.text = "Offline"
        }

        binding.textPhone?.text = getModemsListResponse.phoneNumber.toString()*/

        binding.imageClose.setOnClickListener {
            dismiss()
        }
    }
}