package com.hn_2452.shoes_nike.ui.profile.manage_notification

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentNotificationBinding

class ManageNotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    companion object {
        const val TAG = "Nike:ManageNotificationFragment: "
    }

    private val mManageNotificationViewModel: ManageNotificationViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNotificationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}