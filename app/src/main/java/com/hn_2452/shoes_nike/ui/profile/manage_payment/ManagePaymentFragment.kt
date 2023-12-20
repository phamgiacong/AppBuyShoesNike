package com.hn_2452.shoes_nike.ui.profile.manage_payment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentManagePaymentBinding
import com.hn_2452.shoes_nike.databinding.FragmentPayBinding
import com.hn_2452.shoes_nike.databinding.FragmentPaymentBinding

class ManagePaymentFragment : BaseFragment<FragmentManagePaymentBinding>() {

    companion object {
        const val TAG = "Nike:ManagePaymentFragment: "
    }

    private val mManagePaymentViewModel: ManagePaymentViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentManagePaymentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}