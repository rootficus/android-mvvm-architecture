package com.fionpay.agent.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fionpay.agent.data.model.response.GetMessageManageRecord
import com.fionpay.agent.databinding.SmsBottomSheetBinding
import com.fionpay.agent.utils.Utility
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SmsDetailScreenFragment : BottomSheetDialogFragment() {
    interface BottomDialogEvent {
        fun onAcceptRequest(getMessageManageRecord: GetMessageManageRecord)
        fun onRejectedRequest(getMessageManageRecord: GetMessageManageRecord)
    }

    var listener: BottomDialogEvent? = null
    private lateinit var binding: SmsBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SmsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val getMessageManageRecord =
            arguments?.getSerializable(GetMessageManageRecord::class.java.name) as GetMessageManageRecord
        binding.textSender.text = getMessageManageRecord.sender.toString()
        binding.textAMessage.text = getMessageManageRecord.textMessage.toString()
        binding.textCReceiver.text = getMessageManageRecord.receiver.toString()
        binding.textSmsType.text = getMessageManageRecord.smsType.toString()
        binding.textSimSlot.text = getMessageManageRecord.simSlot.toString()
        binding.textDate.text =
            Utility.convertUtc2Local(getMessageManageRecord.smsDate.toString())

        binding.imageClose.setOnClickListener {
            dismiss()
        }
    }
}