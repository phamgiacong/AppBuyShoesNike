package com.hn_2452.shoes_nike.ui.orders.completed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.MainActivity
import com.hn_2452.shoes_nike.data.model.OrderModel
import com.hn_2452.shoes_nike.databinding.FragmentOrderCompletedBinding
import com.hn_2452.shoes_nike.ui.orders.BaseOrderAdapter
import com.hn_2452.shoes_nike.ui.orders.BaseOrderViewModel

class OrderCompletedFragment : BaseFragment<FragmentOrderCompletedBinding>(), BaseOrderAdapter.OnClick {

    private val orderViewModel: BaseOrderViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOrderCompletedBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        bindView()
        onClickListener()
        observe()
    }

    private fun initData() {
        (requireActivity() as MainActivity).showBottomNav()
    }

    private fun bindView() {

    }

    private fun onClickListener() {

    }

    @Suppress("LABEL_NAME_CLASH")
    private fun observe() {
        orderViewModel.order.observe(viewLifecycleOwner) { order ->
            val list = order.filter { it.orderDelivered }
            if (list.isNotEmpty()){
                mBinding.rcvOrderCompleted.adapter = BaseOrderAdapter(list, requireContext(), this)
                return@observe
            }
            mBinding.layout.visibility = View.VISIBLE
            mBinding.rcvOrderCompleted.visibility = View.GONE

        }
    }

    override fun detail(orderModel: OrderModel) {

    }
}