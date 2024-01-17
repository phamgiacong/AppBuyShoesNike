package com.hn_2452.shoes_nike.ui.notification

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.google.firebase.messaging.FirebaseMessaging
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.NEW_SHOES_NOTIFY
import com.hn_2452.shoes_nike.OFFER_NOTIFY
import com.hn_2452.shoes_nike.OTHER_NOTIFY
import com.hn_2452.shoes_nike.databinding.FragmentNotificationBinding
import com.hn_2452.shoes_nike.ui.home.HomeViewModel
import com.hn_2452.shoes_nike.ui.orders.OrderFragment
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.dpToPx
import com.hn_2452.shoes_nike.utility.handleResource
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
        if (type == DONHANG) {
            mBinding?.donHangLayout?.performClick()
        } else {
            mBinding?.khuyenMaiLayout?.performClick()
        }
        mPass = true
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
            mNotificationItemApdapter.mOnSelect = { notification ->
                Log.i(TAG, "setupNotificationList: $notification")
                mNavController?.navigate(
                    NotificationsFragmentDirections.actionNotificationsFragmentToOrderDetailFragment(
                        notification.link
                    )
                )
            }
            mNotificationItemApdapter.mNotificationViewModel=mNotificationViewModel
            mNotificationItemApdapter.viewLifecycleOwner = viewLifecycleOwner
            mNotificationViewModel.getNotificationOfUser().observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (resource.status) {
                        Status.LOADING -> {
                            Log.i(TAG, "setupNotificationList: loading")
                        }
                        Status.SUCCESS -> {
                            Log.i(TAG, "setupNotificationList: ${resource.data}")
                            if(resource.data.isNullOrEmpty()) {
                                mNotificationItemApdapter.submitList(emptyList())
                                mBinding?.tvNoneItem?.visibility = View.VISIBLE
                            } else {
                                mBinding?.tvNoneItem?.visibility = View.GONE
                                mNotificationItemApdapter.submitList(resource.data)
                            }
                        }
                        Status.ERROR -> {
                            Log.e(TAG, "setupNotificationList: ${resource.message}")
                        }
                    }
                }
            }
        } else {
            mNotificationItemApdapter.mOnSelect = { notification ->
                val notificationType = notification.type
                val objectId = notification.link
                Log.i(TAG, "setupNotificationList: type=$notificationType id=$objectId")
                when (notificationType) {
                    OFFER_NOTIFY -> {
                        handleResource(
                            mHomeViewModel.getOfferById(objectId),
                            lifecycleOwner = viewLifecycleOwner,
                            context = requireContext(),
                            onSuccess = {
                                it?.let {
                                    mNavController?.navigate(
                                        NotificationsFragmentDirections.actionNotificationsFragmentToOfferDetailFragment(it)
                                    )
                                }

                            }
                        )

                    }
                    NEW_SHOES_NOTIFY -> {
                        mNavController?.navigate(
                            NotificationsFragmentDirections.actionNotificationsFragmentToShoesFragment(objectId)
                        )
                    }
                    OTHER_NOTIFY -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle(notification.title)
                            .setMessage(notification.content)
                            .create()
                            .show()
                    }
                }
            }
            mNotificationItemApdapter.mNotificationViewModel=mNotificationViewModel
            mNotificationItemApdapter.viewLifecycleOwner = viewLifecycleOwner
            mNotificationViewModel.getNotificationOffer().observe(viewLifecycleOwner){resources->
                resources?.let {
                    when (resources.status) {
                        Status.LOADING -> {
                            Log.i(TAG, "setupNotificationList: loading}")
                        }

                        Status.SUCCESS -> {
                            Log.i(TAG, "setupNotificationList: ${resources.data}")
                            if(resources.data.isNullOrEmpty()) {
                                mNotificationItemApdapter.submitList(emptyList())
                                mBinding?.tvNoneItem?.visibility = View.VISIBLE
                            } else {
                                mBinding?.tvNoneItem?.visibility = View.GONE
                                mNotificationItemApdapter.submitList(resources.data)
                            }
                        }

                        Status.ERROR -> {
                            Log.e(TAG, "setupNotificationList: ${resources.message}")
                        }
                    }
                }
            }
        }

        mBinding?.rcvNotification?.adapter = mNotificationItemApdapter
    }
}