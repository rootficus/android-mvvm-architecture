package com.jionex.agent.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jionex.agent.data.model.response.GetBalanceManageRecord
import com.jionex.agent.data.model.response.GetMessageManageRecord
import com.jionex.agent.data.model.response.GetModemsByFilterResponse
import com.jionex.agent.databinding.BottomSheetBinding
import com.jionex.agent.databinding.ModemBottomSheetBinding
import com.jionex.agent.databinding.SmsBottomSheetBinding
import com.jionex.agent.utils.Constant
import com.jionex.agent.utils.Utility
import org.jetbrains.annotations.Nullable


class ModemDetailScreenFragment : BottomSheetDialogFragment() {
    interface BottomDialogEvent {
        fun onAcceptRequest(getModemsByFilterResponse: GetModemsByFilterResponse)
        fun onRejectedRequest(getModemsByFilterResponse: GetModemsByFilterResponse)
    }

    var listener: BottomDialogEvent? = null
    private lateinit var binding: ModemBottomSheetBinding

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = ModemBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val getModemsByFilterResponse =
            arguments?.getSerializable(GetMessageManageRecord::class.java.name) as GetModemsByFilterResponse
        binding.textType?.text = getModemsByFilterResponse.smsType.toString()
        binding.textOperator?.text = getModemsByFilterResponse.operator.toString()
        binding.textDeviceId?.text = getModemsByFilterResponse.deviceId.toString()
        binding.textSimId?.text = getModemsByFilterResponse.simId.toString()
        binding.textDeviceInfo?.text = getModemsByFilterResponse.deviceInfo.toString()
        //binding.textAgent?.text =getModemsByFilterResponse.a.toString()
        if (getModemsByFilterResponse.isActive == true) {
            binding.textStatus?.text = "Active"
        }else
        {
            binding.textStatus?.text = "Offline"
        }

        binding.textPhone?.text = getModemsByFilterResponse.phoneNumber.toString()

        binding.imageClose.setOnClickListener {
            dismiss()
        }
    }
}