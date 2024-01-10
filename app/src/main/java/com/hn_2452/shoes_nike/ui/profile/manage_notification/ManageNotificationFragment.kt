package com.hn_2452.shoes_nike.ui.profile.manage_notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.NEW_SHOES_NOTIFY
import com.hn_2452.shoes_nike.OFFER_NOTIFY
import com.hn_2452.shoes_nike.ORDER_NOTIFY
import com.hn_2452.shoes_nike.OTHER_NOTIFY
import com.hn_2452.shoes_nike.databinding.FragmentNotificationSettingBinding
import com.hn_2452.shoes_nike.utility.getBooleanDataByKey
import com.hn_2452.shoes_nike.utility.saveBooleanDataByKey

class ManageNotificationFragment : BaseFragment<FragmentNotificationSettingBinding>() {

    companion object {
        const val TAG = "Nike:ManageNotificationFragment: "
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNotificationSettingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.toolBar)
        loadNotificationSetting()
        setupChangeNotificationSetting()
    }

    private fun loadNotificationSetting() {
        mBinding?.switchNewShoes?.isChecked = getBooleanDataByKey(requireContext(), NEW_SHOES_NOTIFY)
        mBinding?.switchOrder?.isChecked = getBooleanDataByKey(requireContext(), ORDER_NOTIFY)
        mBinding?.switchOffer?.isChecked = getBooleanDataByKey(requireContext(), OFFER_NOTIFY)
        mBinding?.switchOther?.isChecked = getBooleanDataByKey(requireContext(), OTHER_NOTIFY)
    }

    private fun setupChangeNotificationSetting() {
        mBinding?.switchNewShoes?.setOnCheckedChangeListener { _, isChecked ->
            saveBooleanDataByKey(requireContext(), NEW_SHOES_NOTIFY, isChecked)
        }
        mBinding?.switchOffer?.setOnCheckedChangeListener { _, isChecked ->
            saveBooleanDataByKey(requireContext(), OFFER_NOTIFY, isChecked)
        }
        mBinding?.switchOrder?.setOnCheckedChangeListener { _, isChecked ->
            saveBooleanDataByKey(requireContext(), ORDER_NOTIFY, isChecked)
        }
        mBinding?.switchOther?.setOnCheckedChangeListener { _, isChecked ->
            saveBooleanDataByKey(requireContext(), OTHER_NOTIFY, isChecked)
        }
    }
}