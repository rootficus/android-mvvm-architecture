package com.fionpay.agent.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.UpdateActiveInActiveRequest
import com.fionpay.agent.data.model.request.UpdateAvailabilityRequest
import com.fionpay.agent.data.model.request.UpdateLoginRequest
import com.fionpay.agent.data.model.response.GetModemsListResponse
import com.fionpay.agent.databinding.ItemModemsManagerBinding

class ModemsManagerListAdapter(private var itemList: ArrayList<GetModemsListResponse>) :
    RecyclerView.Adapter<ModemsManagerListAdapter.ItemViewHolder>() {

    interface ModemCardEvent {
        fun onStatusClicked(updateActiveInActiveRequest: UpdateActiveInActiveRequest)
        fun onAvailabilityClicked(updateAvailabilityRequest: UpdateAvailabilityRequest)
        fun onLoginClicked(updateLoginRequest: UpdateLoginRequest)
    }

    var listener: ModemCardEvent? = null
    lateinit var context: Context

    inner class ItemViewHolder(val binding: ItemModemsManagerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemModemsManagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: GetModemsListResponse = itemList[position]
        with(holder)
        {
            binding.txtUserName.text =
                "${item.firstName.toString()} ${item.lastName.toString()}"
            binding.txtPinCode.text = item.pinCode.toString()
            binding.currentBalanceAmount.text = "৳${item.balance.toString()}"
            binding.txtTotalCashOut.text = "৳${item.totalCashOut.toString()}"
            binding.txtTodayCashOut.text = "৳${item.todayCashOut.toString()}"
            binding.txtTotalCashIn.text = "৳${item.totalCashIn.toString()}"
            binding.txtTodayCashIn.text = "৳${item.todayCashIn.toString()}"
            binding.txtActive.text = item.status.toString()
            // Login Status
            binding.txtLoggedIn.text = item.loginStatus.toString()
            //Blocked Or Unblocked Status UI
            binding.txtBlock.text = item.availability.toString()
            setBlockUnBlockUI(item, binding)

            //Active Or InActive Status UI
            setActiveInActiveUI(item, binding)

            //Login Or Logout Status UI
            setLoginLogoutUI(item, binding)

        }

    }

    private fun setLoginLogoutUI(item: GetModemsListResponse, binding: ItemModemsManagerBinding) {


        if (binding.txtLoggedIn.text.toString().contains("Logout", true)) {
            binding.txtLoggedIn.setTextColor(ContextCompat.getColor(context, R.color.reject))
            binding.txtLoggedIn.backgroundTintList =
                ContextCompat.getColorStateList(
                    context,
                    R.color.activeDangerBg
                )

        } else {
            binding.txtLoggedIn.setTextColor(ContextCompat.getColor(context, R.color.greenColor))
            binding.txtLoggedIn.backgroundTintList =
                ContextCompat.getColorStateList(
                    context,
                    R.color.activeGreenBg
                )
        }
        binding.txtLoggedIn.isFocusable = !item.loginStatus.toString().contains("Logout")
        binding.txtLoggedIn.isClickable = !item.loginStatus.toString().contains("Logout")
        binding.txtLoggedIn.setOnClickListener {
            val loginOrLogout: String =
                if (binding.txtLoggedIn.text.toString().contains("Logout", true)) {
                    "Login"

                } else {
                    "Logout"
                }
            listener?.onLoginClicked(UpdateLoginRequest(item.id.toString(), loginOrLogout))
        }
    }

    private fun setActiveInActiveUI(
        item: GetModemsListResponse,
        binding: ItemModemsManagerBinding
    ) {

        if (binding.txtActive.text.toString() == "Active") {
            binding.txtActive.setTextColor(ContextCompat.getColor(context, R.color.greenColor))
            binding.txtActive.backgroundTintList =
                ContextCompat.getColorStateList(
                    context,
                    R.color.activeGreenBg
                )

        } else {
            binding.txtActive.setTextColor(ContextCompat.getColor(context, R.color.reject))
            binding.txtActive.backgroundTintList =
                ContextCompat.getColorStateList(
                    context,
                    R.color.activeDangerBg
                )
        }
        binding.txtActive.setOnClickListener {
            val activeOrInactive: String =
                if (binding.txtActive.text.toString() == "Active") {
                    "Inactive"
                } else {
                    "Active"
                }
            listener?.onStatusClicked(
                UpdateActiveInActiveRequest(
                    item.id.toString(),
                    activeOrInactive
                )
            )
        }
    }

    private fun setBlockUnBlockUI(item: GetModemsListResponse, binding: ItemModemsManagerBinding) {
        if (item.availability.toString() == "Off") {
            binding.txtBlock.setTextColor(ContextCompat.getColor(context, R.color.reject))
            binding.txtBlock.backgroundTintList =
                ContextCompat.getColorStateList(
                    context,
                    R.color.activeDangerBg
                )
        } else {
            binding.txtBlock.setTextColor(ContextCompat.getColor(context, R.color.greenColor))
            binding.txtBlock.backgroundTintList =
                ContextCompat.getColorStateList(
                    context,
                    R.color.activeGreenBg
                )
        }


        binding.txtBlock.setOnClickListener {
            val blockOrUnblocked: String =
                if (binding.txtBlock.text.toString() == "Off") {
                    "On"
                } else {
                    "Off"
                }
            listener?.onAvailabilityClicked(
                UpdateAvailabilityRequest(
                    item.id.toString(),
                    blockOrUnblocked
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateListFilter(listGetModemsByFilter: ArrayList<GetModemsListResponse>) {
        itemList = listGetModemsByFilter
        notifyDataSetChanged()

    }

}