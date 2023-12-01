package com.fionpay.agent.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.databinding.BlBottomSheetBinding
import com.fionpay.agent.utils.Constant
import com.fionpay.agent.utils.Utility
import org.jetbrains.annotations.Nullable


class BalanceDetailScreenFragment: BottomSheetDialogFragment() {
    interface BottomDialogEvent {
        fun onAcceptRequest(getBalanceManageRecord: GetBalanceManageRecord)
        fun onRejectedRequest(getBalanceManageRecord: GetBalanceManageRecord)
    }

    var listener: BottomDialogEvent? = null
    private lateinit var binding: BlBottomSheetBinding
    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = BlBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val getBalanceManageRecord = arguments?.getSerializable(GetBalanceManageRecord::class.java.name) as GetBalanceManageRecord
        binding.textAAccount.text = getBalanceManageRecord.agentAccountNo.toString()
        binding.textAmount.text = getBalanceManageRecord.amount.toString()
        binding.textCAccount.text = getBalanceManageRecord.customerAccountNo.toString()
        binding.textCommission.text = getBalanceManageRecord.commision.toString()
        binding.textDate.text =
            Utility.convertUtc2Local(getBalanceManageRecord.date.toString())
        binding.textSender.text = getBalanceManageRecord.sender.toString()
        binding.textLastBalance.text = getBalanceManageRecord.lastBalance.toString()
        binding.textOldBalance.text = getBalanceManageRecord.oldBalance.toString()
        binding.textType.text = getBalanceManageRecord.bType.toString()
        binding.textStatus.text = getBalanceManageRecord.status.toString()
        binding.textLastTransactionId.text = getBalanceManageRecord.transactionId
        if (getBalanceManageRecord.status.equals(Constant.BalanceManagerStatus.PENDING.toString(),true)) {
            binding.btnReject.visibility = View.VISIBLE
            binding.btnAccept.visibility = View.VISIBLE
        } else {
            binding.btnReject.visibility = View.GONE
            binding.btnAccept.visibility = View.GONE
        }
        binding.btnReject.setOnClickListener {
            dismiss()
            listener?.onRejectedRequest(getBalanceManageRecord)
        }
        binding.btnAccept.setOnClickListener {
            dismiss()
            listener?.onAcceptRequest(getBalanceManageRecord)
        }
        binding.imageClose.setOnClickListener {
            dismiss()
        }
    }
}