package com.hn_2452.shoes_nike.ui.orders.active

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.MainActivity
import com.hn_2452.shoes_nike.MainViewModel
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.OrderModel
import com.hn_2452.shoes_nike.databinding.FragmentOrderActiveBinding
import com.hn_2452.shoes_nike.ui.orders.BaseOrderAdapter
import com.hn_2452.shoes_nike.ui.orders.BaseOrderViewModel

class OrderActiveFragment : BaseFragment<FragmentOrderActiveBinding>(), BaseOrderAdapter.OnClick {

    private val orderViewModel: BaseOrderViewModel by viewModels()
    private val sendOrderViewModel: MainViewModel by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOrderActiveBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        bindView()
        onClickListener()
        observeData()
    }

    private fun initData() {
        (requireActivity() as MainActivity).showBottomNav()
    }

    private fun bindView() {
    }

    private fun onClickListener() {

    }

    private fun observeData() {
        orderViewModel.order.observe(viewLifecycleOwner) { order ->
            val list = order.filter { !it.orderDelivered }
            if (list.isNotEmpty()){
                mBinding.rcvOrderActive.adapter = BaseOrderAdapter(list, requireContext(), this)
                return@observe
            }
            mBinding.layout.visibility = View.VISIBLE
            mBinding.rcvOrderActive.visibility = View.GONE
        }
    }

    override fun detail(orderModel: OrderModel) {
        sendOrderViewModel.sendOrder.postValue(orderModel)
        findNavController().navigate(R.id.action_orderFragment_to_trackOrderFragment)
    }

}