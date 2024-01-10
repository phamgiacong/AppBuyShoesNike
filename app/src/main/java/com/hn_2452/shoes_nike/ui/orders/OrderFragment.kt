package com.hn_2452.shoes_nike.ui.orders

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentOrderBinding
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.dpToPx
import com.hn_2452.shoes_nike.utility.handleResource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding>() {

    private val mOrderViewModel: OrderViewModel by viewModels()

    @Inject
    lateinit var mOrderItemAdapter: OrderItemAdapter

    companion object {
        const val ACTIVE = 0
        const val COMPLETE = 1
        const val TAG = "Nike:OrderFragment: "
        var type = ACTIVE
    }

    private var mCurrentStateOrder = ACTIVE
    private var mPass = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mPass = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentOrderBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUser()
        setupLoading(mBinding?.loadingProgress)
        setupBottomBar(true)
        setupIndicator()
        setupOrderList()
    }

    override fun onStart() {
        super.onStart()
        setupBottomBar(true)
        if(type == ACTIVE) {
            mBinding?.activeLayout?.performClick()
        } else {
            mBinding?.completeLayout?.performClick()
        }
    }

    private fun setupUser() {
        mOrderViewModel.mCurrentUser.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                mBinding?.mainLayout?.visibility = View.VISIBLE
                mBinding?.layoutNeedLogin?.visibility = View.GONE
            } else {
                mBinding?.mainLayout?.visibility = View.GONE
                mBinding?.layoutNeedLogin?.visibility = View.VISIBLE
                mBinding?.btnSignIn?.setOnClickListener {
                    mNavController?.navigate(R.id.loginFragment)
                }
            }
        }
    }

    private fun setupOrderList() {
        mOrderItemAdapter.mOnDetail = { order ->
           mNavController?.navigate(
               OrderFragmentDirections.actionOrderFragmentToOrderDetailFragment(order.id)
           )
        }
        mBinding?.rcvCartItem?.adapter = mOrderItemAdapter
    }

    private fun loadOrderOfUser(active: Boolean) {
        Log.i(TAG, "loadOrderOfUser: active = $active")
        handleResource(
            data = mOrderViewModel.getOrderOfUser(active),
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            isErrorInform = false,
            onLoading = {startLoading()},
            onError = {stopLoading()},
            onSuccess = { orders ->
                stopLoading()
                if(orders.isNullOrEmpty()) {
                    mOrderItemAdapter.submitList(emptyList())
                    mBinding?.tvNoneOrder?.visibility = View.VISIBLE
                } else {
                    mBinding?.tvNoneOrder?.visibility = View.GONE
                    mBinding?.mainLayout?.visibility = View.VISIBLE
                    mOrderItemAdapter.submitList(orders)
                }
            }
        )
    }

    private fun setupIndicator() {
        mCurrentStateOrder = ACTIVE
        mBinding?.activeLayout?.setOnClickListener {
            if(mCurrentStateOrder == COMPLETE || mPass) {
                mPass = false
                mCurrentStateOrder = ACTIVE
                type = ACTIVE
                mBinding?.tvActive?.setTextColor(Color.BLACK)
                mBinding?.lineActive?.setBackgroundColor(Color.BLACK)
                val layoutParams = mBinding?.lineActive?.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.height = dpToPx(requireContext(), 4)
                layoutParams.setMargins(0, dpToPx(requireContext(), 8), 0, 0)
                mBinding?.lineActive?.requestLayout()
                //
                mBinding?.tvComplete?.setTextColor(Color.LTGRAY)
                mBinding?.lineComplete?.setBackgroundColor(Color.LTGRAY)
                val completeLayoutParams = mBinding?.lineComplete?.layoutParams as ViewGroup.MarginLayoutParams
                completeLayoutParams.height = dpToPx(requireContext(), 2)
                completeLayoutParams.setMargins(0, dpToPx(requireContext(), 9), 0, 0)
                mBinding?.lineComplete?.requestLayout()
                loadOrderOfUser(true)
            }
        }

        mBinding?.completeLayout?.setOnClickListener {
            if(mCurrentStateOrder == ACTIVE || mPass) {
                mPass = false
                mCurrentStateOrder = COMPLETE
                type = COMPLETE
                mBinding?.tvActive?.setTextColor(Color.LTGRAY)
                mBinding?.lineActive?.setBackgroundColor(Color.LTGRAY)
                val layoutParams = mBinding?.lineActive?.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.height = dpToPx(requireContext(), 2)
                layoutParams.setMargins(0, dpToPx(requireContext(), 9), 0, 0)
                mBinding?.lineActive?.requestLayout()
                //
                mBinding?.tvComplete?.setTextColor(Color.BLACK)
                mBinding?.lineComplete?.setBackgroundColor(Color.BLACK)
                val completeLayoutParams = mBinding?.lineComplete?.layoutParams as ViewGroup.MarginLayoutParams
                completeLayoutParams.height = dpToPx(requireContext(), 4)
                completeLayoutParams.setMargins(0, dpToPx(requireContext(), 8), 0, 0)
                mBinding?.lineComplete?.requestLayout()
                loadOrderOfUser(false)
            }

        }
    }

    override fun onStop() {
        super.onStop()
        setupBottomBar(false)
    }

}