package com.hn_2452.shoes_nike.ui.cart.check_out

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.databinding.FragmentCheckOutBinding
import com.hn_2452.shoes_nike.utility.handleResource
import com.hn_2452.shoes_nike.utility.toDayString
import com.hn_2452.shoes_nike.utility.toVND
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CheckOutFragment : BaseFragment<FragmentCheckOutBinding>() {

    companion object {
        const val TAG = "Nike:CheckOutFragment: "
    }

    private val mCheckOutViewModel: CheckOutViewModel by activityViewModels()

    @Inject
    lateinit var mOrderItemAdapter: OrderItemAdapter


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCheckOutBinding.inflate(inflater, container, false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")
        setupBackBtn()
        setupSelectAddress()
        setupAddressData()
        setupOrderList()
        setupOffer()
        setupPaymentMethod()
        setupOrderButton()
    }

    private fun setupOrderButton() {
        mBinding?.btnOrder?.setOnClickListener {
            if (mCheckOutViewModel.mCurrentPaymentMethod == 0) {
                handleResource(
                    data = mCheckOutViewModel.putNewOrder(),
                    lifecycleOwner = viewLifecycleOwner,
                    context = requireContext(),
                    onSuccess = {
                        Toast.makeText(
                            requireContext(),
                            "Đặt đơn hàng thành công",
                            Toast.LENGTH_LONG
                        ).show()
                        mNavController?.popBackStack()
                    }
                )
            } else {
                // todo: online payment
            }
        }
    }

    private fun setupPaymentMethod() {
        mBinding?.cashPayment?.setOnClickListener {
            mCheckOutViewModel.mCurrentPaymentMethod = 0
            mBinding?.cashPayment?.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
            mBinding?.cashPayment?.setTextColor(Color.WHITE)
            mBinding?.onlinePayment?.backgroundTintList = null
            mBinding?.onlinePayment?.setTextColor(Color.BLACK)
        }

        mBinding?.onlinePayment?.setOnClickListener {
            mCheckOutViewModel.mCurrentPaymentMethod = 1
            mBinding?.onlinePayment?.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
            mBinding?.onlinePayment?.setTextColor(Color.WHITE)
            mBinding?.cashPayment?.backgroundTintList = null
            mBinding?.cashPayment?.setTextColor(Color.BLACK)
        }
    }

    private fun setupOffer() {
        mCheckOutViewModel.mCurrentOffer.observe(requireActivity()) { offer ->
            offer?.let {
                Log.i(TAG, "setupOffer: $mBinding")
                mBinding?.run {
                    noneOffer.visibility = View.GONE
                    val saleString = if (offer.discountUnit == 0) {
                        "Giảm giá ${offer.discount}%"
                    } else {
                        "Giảm giá ${offer.discount.toVND()}"
                    }

                    val saleMore = if (offer.maxDiscount != -1L) {
                        "Giảm tối đa ${offer.maxDiscount.toVND()} cho đơn hàng từ ${offer.valueToApply.toVND()}"
                    } else {
                        "Áp dụng cho đơn hàng từ ${offer.valueToApply.toVND()}"
                    }

                    tvSalePrice.text = saleString
                    tvSaleMore.text = saleMore
                    tvTitle.text = offer.title
                    tvExpired.text = "Hết hạn ${offer.endTime.toDayString()}"
                    tvDetail.setOnClickListener {
                        mNavController?.navigate(
                            CheckOutFragmentDirections.actionCheckOutFragmentToOfferDetailFragment(
                                offer
                            )
                        )
                    }

                    val currentPrice = mCheckOutViewModel.mPrice
                    if (currentPrice != -1L) {
                        if (offer.discountUnit == 0) {
                            var sale = (currentPrice * offer.discount / 100)

                            if (offer.maxDiscount != -1L && sale >= offer.maxDiscount) {
                                // so tien khuyen mai >= so tien duoc phep khuyen mai
                                sale = offer.maxDiscount
                                mBinding?.tvSale?.text = sale.toVND()
                            } else {
                                // khong gioi han so tien khuyen mai
                                // so tien < so tien duoc khuyen mai
                                mBinding?.tvSale?.text = sale.toVND()
                            }

                            val totalPrice = currentPrice - sale
                            mBinding?.tvTotalPrice?.text = totalPrice.toVND()
                            mCheckOutViewModel.mTotalPrice = totalPrice
                        } else {
                            // tong tien =  so tien - so tien khuyen mai
                            // so tien khuyen mai = so tien khuyen mai
                            mBinding?.tvSale?.text = offer.discount.toVND()
                            val totalPrice = (currentPrice - offer.discount)
                            mBinding?.tvTotalPrice?.text = totalPrice.toVND()
                            mCheckOutViewModel.mTotalPrice = totalPrice
                        }
                    }

                    currentOffer.visibility = View.VISIBLE
                }

            }
        }

        mBinding?.tvSelectOffer?.setOnClickListener {
            if (mCheckOutViewModel.mPrice != -1L) {
                mNavController?.navigate(R.id.appliedOfferFragment)
            }
        }
    }

    private fun setupOrderList() {
        mBinding?.rcvShoesToCart?.adapter = mOrderItemAdapter
        loadOrderItemOfUser()
    }

    private fun loadOrderItemOfUser() {
        handleResource(
            data = mCheckOutViewModel.getCartOfUser(),
            lifecycleOwner = viewLifecycleOwner,
            onLoading = { startLoading() },
            isErrorInform = true,
            onError = { stopLoading() },
            onSuccess = { orderItemList ->
                stopLoading()
                mCheckOutViewModel.mCurrentOrderDetailList = orderItemList
                mOrderItemAdapter.submitList(orderItemList)
                val currentPrice = calculatePrice(orderItemList)
                mCheckOutViewModel.mPrice = currentPrice
                mBinding?.tvAmount?.text = currentPrice.toVND()
                mBinding?.tvTotalPrice?.text = currentPrice.toVND()

                val currentOffer = mCheckOutViewModel.mCurrentOffer.value
                if (currentOffer != null) {
                    if (currentOffer.discountUnit == 0) {
                        var sale = (currentPrice * currentOffer.discount / 100)

                        if (currentOffer.maxDiscount != -1L && sale >= currentOffer.maxDiscount) {
                            // so tien khuyen mai >= so tien duoc phep khuyen mai
                            sale = currentOffer.maxDiscount
                            mBinding?.tvSale?.text = sale.toVND()
                        } else {
                            // khong gioi han so tien khuyen mai
                            // so tien < so tien duoc khuyen mai
                            mBinding?.tvSale?.text = sale.toVND()
                        }

                        val totalPrice = currentPrice - sale
                        mBinding?.tvTotalPrice?.text = totalPrice.toVND()
                        mCheckOutViewModel.mTotalPrice = totalPrice
                    } else {
                        // tong tien =  so tien - so tien khuyen mai
                        // so tien khuyen mai = so tien khuyen mai
                        mBinding?.tvSale?.text = currentOffer.discount.toVND()
                        val totalPrice = (currentPrice - currentOffer.discount)
                        mBinding?.tvTotalPrice?.text = totalPrice.toVND()
                        mCheckOutViewModel.mTotalPrice = totalPrice
                    }
                } else {
                    getOfferOfUser()
                }
            },
            context = requireContext()
        )
    }

    private fun calculatePrice(orderItemList: List<OrderDetail>?): Long {
        var price = 0L
        orderItemList?.forEach {

            val discount: Long = if (it.shoes.discountUnit == 0) {
                (it.shoes.price * it.shoes.discount / 100).toLong()
            } else {
                it.shoes.discount
            }
            price += (it.quantity * (it.shoes.price - discount)).toLong()
        }
        return price
    }

    private fun setupAddressData() {
        mCheckOutViewModel.mCurrentAddress.observe(viewLifecycleOwner) { address ->
            address?.let {
                mBinding?.run {
                    noneAddress.visibility = View.GONE
                    tvAddress.text = address.address
                    tvNameAddress.text = address.name
                    tvPhoneNumber.text = address.phoneNumber
                    val more = if (address.type == 0) {
                        "Nhà riêng"
                    } else {
                        "Văn phòng"
                    }
                    tvMore.text = more
                    currentAddress.visibility = View.VISIBLE
                }
            }
        }


        if (mCheckOutViewModel.mCurrentAddress.value == null) {
            handleResource(
                data = mCheckOutViewModel.getDefaultAddress(),
                lifecycleOwner = viewLifecycleOwner,
                context = requireContext(),
                onSuccess = { address ->
                    mCheckOutViewModel.mCurrentAddress.value = address
                }
            )
        }

    }

    private fun setupSelectAddress() {
        mBinding?.tvSelectAddress?.setOnClickListener {
            mNavController?.navigate(R.id.addressFragment)
        }
    }

    private fun getOfferOfUser() {
        Log.i(TAG, "getOfferOfUser: ")
        handleResource(
            data = mCheckOutViewModel.getOfferOfUser(),
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            onSuccess = { offers ->
                takeASuitableOffer(offers)
            }
        )
    }

    private fun takeASuitableOffer(offers: List<Offer>?) {
        offers?.run {
            val newOffers = offers.sortedByDescending { it.maxDiscount }
            newOffers.forEach { offer ->
                if (offer.maxDiscount <= mCheckOutViewModel.mPrice) {
                    mCheckOutViewModel.mCurrentOffer.value = offer
                    return@forEach
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }

    private fun setupBackBtn() {
        mBinding?.imvNavigationIcon?.setOnClickListener {
            mNavController?.popBackStack()
        }
    }
}