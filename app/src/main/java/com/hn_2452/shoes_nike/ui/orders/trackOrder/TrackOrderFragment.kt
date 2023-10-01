package com.hn_2452.shoes_nike.ui.orders.trackOrder

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.MainActivity
import com.hn_2452.shoes_nike.MainViewModel
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentTrackOrderBinding
import com.hn_2452.shoes_nike.extension.setSafeOnClickListener

class TrackOrderFragment : BaseFragment<FragmentTrackOrderBinding>() {

    private val sendOrderViewModel : MainViewModel by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTrackOrderBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        bindView()
        onClickListener()
        observe()
    }

    private fun initData() {
        (requireActivity() as MainActivity).hideBottomNav()
    }

    private fun bindView() {

    }

    private fun onClickListener() {
        mBinding.imgBack.setSafeOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        sendOrderViewModel.sendOrder.observe(viewLifecycleOwner) {

            Glide.with(requireContext())
                .load(it.imageProduct)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(mBinding.imgProduct)
            mBinding.tvNameProduct.text = it.nameProduct
            mBinding.viewColor.background.setTint(Color.parseColor(it.colorProduct))
            mBinding.tvNameColor.text = it.colorProduct
            mBinding.tvSize.text = "Size = ${it.sizeProduct}"
            mBinding.tvQuantity.text = "Qty = ${it.quantity}"
            mBinding.tvTotal.text = "$${it.total}"
        }
    }
}