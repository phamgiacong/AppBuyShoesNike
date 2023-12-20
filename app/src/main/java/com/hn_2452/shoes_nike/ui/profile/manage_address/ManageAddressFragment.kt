package com.hn_2452.shoes_nike.ui.profile.manage_address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentManageAddresBinding

class ManageAddressFragment : BaseFragment<FragmentManageAddresBinding>() {

    companion object {
        const val TAG = "Nike:ManageAddressFragment: "
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentManageAddresBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



}