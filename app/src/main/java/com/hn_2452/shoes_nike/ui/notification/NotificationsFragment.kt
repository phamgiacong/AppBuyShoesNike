package com.hn_2452.shoes_nike.ui.notification

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentNotificationBinding
import com.hn_2452.shoes_nike.ui.home.HomeViewModel
import com.hn_2452.shoes_nike.ui.orders.OrderFragment
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsFragment : BaseFragment<FragmentNotificationBinding>() {
    private val mNotificationViewModel: NotificationViewModel by viewModels()
    private val mHomeViewModel: HomeViewModel by viewModels()

    companion object {
        const val DONHANG = 0
        const val KHUYENMAI = 1
        const val TAG = "Nike:OrderFragment: "
        var type = DONHANG
    }

    private var mCurrentStateOrder = DONHANG
    private var mPass = true

    @Inject
    lateinit var mNotificationItemApdapter: NotificationItemApdapter
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNotificationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.toolbar)
        setupIndicator()

    }

    override fun onStart() {
        super.onStart()
        if (OrderFragment.type == DONHANG) {
            mBinding?.donHangLayout?.performClick()
        } else {
            mBinding?.khuyenMaiLayout?.performClick()
        }
    }

    private fun setupIndicator() {
        mCurrentStateOrder = DONHANG
        mBinding?.donHangLayout?.setOnClickListener {
            if (mCurrentStateOrder == KHUYENMAI || mPass) {
                mPass = false
                mCurrentStateOrder = DONHANG
                type = DONHANG
                mBinding?.tvDonHang?.setTextColor(Color.BLACK)
                mBinding?.lineDonHang?.setBackgroundColor(Color.BLACK)
                val layoutParams =
                    mBinding?.lineDonHang?.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.height = dpToPx(requireContext(), 4)
                layoutParams.setMargins(0, dpToPx(requireContext(), 8), 0, 0)
                mBinding?.lineDonHang?.requestLayout()
                //
                mBinding?.tvKhuyenMai?.setTextColor(Color.LTGRAY)
                mBinding?.lineKhuyenMai?.setBackgroundColor(Color.LTGRAY)
                val completeLayoutParams =
                    mBinding?.lineKhuyenMai?.layoutParams as ViewGroup.MarginLayoutParams
                completeLayoutParams.height = dpToPx(requireContext(), 2)
                completeLayoutParams.setMargins(0, dpToPx(requireContext(), 9), 0, 0)
                mBinding?.lineKhuyenMai?.requestLayout()
                setupNotificationList(0)
            }
        }
        mBinding?.khuyenMaiLayout?.setOnClickListener {
            if (mCurrentStateOrder == DONHANG || mPass) {
                mPass = false
                mCurrentStateOrder = KHUYENMAI
                type = KHUYENMAI
                mBinding?.tvDonHang?.setTextColor(Color.LTGRAY)
                mBinding?.lineDonHang?.setBackgroundColor(Color.LTGRAY)
                val layoutParams =
                    mBinding?.lineDonHang?.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.height = dpToPx(requireContext(), 2)
                layoutParams.setMargins(0, dpToPx(requireContext(), 9), 0, 0)
                mBinding?.lineDonHang?.requestLayout()
                //
                mBinding?.tvKhuyenMai?.setTextColor(Color.BLACK)
                mBinding?.lineKhuyenMai?.setBackgroundColor(Color.BLACK)
                val completeLayoutParams =
                    mBinding?.lineKhuyenMai?.layoutParams as ViewGroup.MarginLayoutParams
                completeLayoutParams.height = dpToPx(requireContext(), 4)
                completeLayoutParams.setMargins(0, dpToPx(requireContext(), 8), 0, 0)
                mBinding?.lineKhuyenMai?.requestLayout()
                setupNotificationList(1)
            }

        }

    }

    private fun setupNotificationList(type: Int) {
        if (type == 0) {
            mNotificationItemApdapter.mOnSelect = { id ->
                Log.e(TAG, "setupNotificationList: click")
                id?.let {
                    mNavController?.navigate(
                        NotificationsFragmentDirections.actionNotificationsFragmentToOrderDetailFragment(
                            id
                        )
                    )
                }
            }
            mHomeViewModel.mUsers.observe(viewLifecycleOwner) { users ->
                if (users != null && users.isNotEmpty()) {
                    mNotificationViewModel.getNotificationOfUser(users[0].id)
                        .observe(viewLifecycleOwner) { resource ->
                            resource?.let {
                                when (resource.status) {
                                    Status.LOADING -> {
                                        Log.i(
                                            TAG,
                                            "setupNotificationList: loading ${users[0].id}",
                                        )
                                    }

                                    Status.SUCCESS -> {
                                        Log.i(TAG, "setupNotificationList: ")
                                        resource.data?.let {
                                            it.data.let {
                                                mNotificationItemApdapter.submitList(it)
                                            }
                                        }
                                    }

                                    Status.ERROR -> {
                                    }
                                }
                            }
                        }

                } else {
                    Log.i(TAG, "updateUserInfo: user is null")
                }
            }
        } else {
            mNotificationItemApdapter.mOnSelect = { id ->
            }
            mNotificationViewModel.getNotificationOffer().observe(viewLifecycleOwner) { resources ->
                resources?.let {
                    when (resources.status) {
                        Status.LOADING -> {
                            Log.i(TAG, "setupNotificationList: loading}")
                        }

                        Status.SUCCESS -> {
                            Log.i(TAG, "setupNotificationList: ${resources.data}")
                            if (resources.data == null) {
                                mBinding?.rcvNotification?.visibility = View.GONE
                            } else {
                                mNotificationItemApdapter.submitList(resources.data)
                            }
                        }

                        Status.ERROR -> {
                            Log.i(TAG, "setupNotificationList: ${resources.message}")
                        }
                    }
                }
            }
        }


        mBinding?.rcvNotification?.adapter = mNotificationItemApdapter

    }
}