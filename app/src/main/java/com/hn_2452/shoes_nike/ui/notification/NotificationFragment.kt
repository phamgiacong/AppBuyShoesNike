package com.hn_2452.shoes_nike.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentNotificationBinding
import com.hn_2452.shoes_nike.utility.Status

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    private val mNotificationViewModel: NotificationViewModel by viewModels()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNotificationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.toolbar)
        setupNotificationList()
    }

    private fun setupNotificationList() {
        val controller = NotificationAdapterController()
        mBinding?.rcvNotification?.setController(controller)
        mNotificationViewModel.getNotification().observe(viewLifecycleOwner) { resource ->
            resource?.let {
                when (resource.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        resource.data?.let {
                            controller.setData(resource.data)
                        }
                    }

                    Status.ERROR -> {

                    }
                }
            }
        }
    }

}