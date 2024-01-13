package com.hn_2452.shoes_nike.ui.cart.check_out

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.zalopaykotlin.Api.CreateOrder
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.UserOffer
import com.hn_2452.shoes_nike.databinding.FragmentBuyNowBinding
import com.hn_2452.shoes_nike.databinding.LayoutOrderItemBinding
import com.hn_2452.shoes_nike.utility.handleResource
import com.hn_2452.shoes_nike.utility.toDayString
import com.hn_2452.shoes_nike.utility.toVND
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

@AndroidEntryPoint
class BuyNowFragment : BaseFragment<FragmentBuyNowBinding>() {
    private var strPrice :String?=null
    companion object {
        const val TAG = "Nike:CheckOutFragment: "
    }

    private val mCheckOutViewModel: CheckOutViewModel by activityViewModels()

    private val mArgs : BuyNowFragmentArgs by navArgs()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBuyNowBinding.inflate(inflater, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")
        setupLoading(mBinding?.loadingProgress)
        setupOrderItem()
        setupBackBtn()
        setupSelectAddress()
        setupAddressData()
        setupPaymentMethod()
        setupOrderButton()
        setupOffer()
        getOfferOfUser()

    }

    private fun setupOrderItem() {
        val orderItem = mArgs.orderItem
        mCheckOutViewModel.mCurrentOrderDetail = orderItem
        val orderItemBinding: LayoutOrderItemBinding = mBinding?.orderItem as LayoutOrderItemBinding
        orderItemBinding.run {
            imgProduct.load(orderItem.shoes.main_image) {
                error(R.drawable.ic_launcher_background)
            }

            tvNameProduct.text = orderItem.shoes.name
            tvPriceProduct.text = orderItem.shoes.finalPrice.toVND()
            tvSize.text = getString(R.string.shoes_size, orderItem.size)
            tvQuantity.text = getString(R.string.shoes_quantity, orderItem.quantity)
            cavColor.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(orderItem.color)))
        }

        val discount: Long = if (orderItem.shoes.discountUnit == 0) {
            (orderItem.shoes.price * orderItem.shoes.discount / 100).toLong()
        } else {
            orderItem.shoes.discount
        }
        val price = (orderItem.quantity * (orderItem.shoes.price - discount)).toLong()
        mBinding?.tvAmount?.text = price.toVND()
        mBinding?.tvTotalPrice?.text = price.toVND()
        mCheckOutViewModel.mPrice = price
        mCheckOutViewModel.mTotalPrice = price
    }

    private fun setupOrderButton() {
        mBinding?.btnOrder?.setOnClickListener {
            if (mCheckOutViewModel.mCurrentPaymentMethod == 0) {
                handleResource(
                    data = mCheckOutViewModel.putNewOrderByRawData(),
                    lifecycleOwner = viewLifecycleOwner,
                    context = requireContext(),
                    onSuccess = {
                        stopLoading()
                        Toast.makeText(
                            requireContext(),
                            "Đặt đơn hàng thành công",
                            Toast.LENGTH_LONG
                        ).show()
                        mCheckOutViewModel.clearData()
                        mNavController?.navigate(
                            BuyNowFragmentDirections.actionBuyNowFragmentToHomeFragment()
                        )
                    },
                    isErrorInform = true,
                    onLoading = {startLoading()},
                    onError = {stopLoading()}
                )
            } else {
                val orderApi = CreateOrder()
                val data: JSONObject? = strPrice?.let { it1 -> orderApi.createOrder(it1) }
                val code = data?.getString("returncode")
                if (code == "1") {
                    val token = data?.getString("zptranstoken")
                    token?.let { it1 ->
                        ZaloPaySDK.getInstance()
                            .payOrder(
                                requireActivity(),
                                it1,
                                "demozpdk://app",
                                object : PayOrderListener {
                                    override fun onPaymentSucceeded(
                                        transactionId: String,
                                        transToken: String,
                                        appTransID: String
                                    ) {
                                        requireActivity().runOnUiThread() {
                                            Log.e("TAG", "onPaymentSucceeded: ")
                                            AlertDialog.Builder(requireContext())
                                                .setTitle("Payment Success")
                                                .setMessage(
                                                    String.format(
                                                        "Thanh toán thành công"
                                                    )
                                                )
                                                .setPositiveButton(
                                                    "OK"
                                                ) { dialog, which ->
                                                    handleResource(
                                                        data = mCheckOutViewModel.putNewOrder(),
                                                        lifecycleOwner = viewLifecycleOwner,
                                                        context = requireContext(),
                                                        onSuccess = {
                                                            stopLoading()
                                                            Toast.makeText(
                                                                requireContext(),
                                                                "Đặt đơn hàng thành công",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                            mNavController?.navigate(
                                                                CheckOutFragmentDirections.actionCheckOutFragmentToHomeFragment()
                                                            )
                                                        },
                                                        isErrorInform = true,
                                                        onLoading = {startLoading()},
                                                        onError = {stopLoading()}
                                                    )

                                                }
                                                .setNegativeButton("Cancel", null).show()
                                        }

                                    }

                                    override fun onPaymentCanceled(
                                        zpTransToken: String,
                                        appTransID: String
                                    ) {
                                        AlertDialog.Builder(requireContext())
                                            .setTitle("User Cancel Payment")
                                            .setMessage(
                                                String.format(
                                                  "Đã thoát thanh toán "
                                                )
                                            )
                                            .setPositiveButton(
                                                "OK"
                                            ) { dialog, which -> }
                                            .setNegativeButton("Cancel", null).show()
                                    }

                                    override fun onPaymentError(
                                        zaloPayError: ZaloPayError,
                                        zpTransToken: String,
                                        appTransID: String
                                    ) {
                                        AlertDialog.Builder(requireContext())
                                            .setTitle("Payment Fail")
                                            .setMessage(
                                                String.format(
                                                  "Lỗi thanh toán"
                                                )
                                            )
                                            .setPositiveButton(
                                                "OK"
                                            ) { dialog, which -> }
                                            .setNegativeButton("Cancel", null).show()
                                    }
                                })
                    }
                }

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
        mCheckOutViewModel.mCurrentOffer.observe(requireActivity()) { userOffer ->
            userOffer?.let {
                Log.i(TAG, "setupOffer: $mBinding")
                mBinding?.run {
                    noneOffer.visibility = View.GONE
                    val saleString = if (userOffer.offer.discountUnit == 0) {
                        "Giảm giá ${userOffer.offer.discount}%"
                    } else {
                        "Giảm giá ${userOffer.offer.discount.toVND()}"
                    }

                    val saleMore = if (userOffer.offer.maxDiscount != -1L) {
                        "Giảm tối đa ${userOffer.offer.maxDiscount.toVND()} cho đơn hàng từ ${userOffer.offer.valueToApply.toVND()}"
                    } else {
                        "Áp dụng cho đơn hàng từ ${userOffer.offer.valueToApply.toVND()}"
                    }

                    tvSalePrice.text = saleString
                    tvSaleMore.text = saleMore
                    tvTitle.text = userOffer.offer.title
                    tvExpired.text = "Hết hạn ${userOffer.offer.endTime.toDayString()}"
                    tvDetail.setOnClickListener {
                        mNavController?.navigate(
                            CheckOutFragmentDirections.actionCheckOutFragmentToOfferDetailFragment(
                                userOffer.offer
                            )
                        )
                    }

                    val currentPrice = mCheckOutViewModel.mPrice
                    if (currentPrice != -1L) {
                        if (userOffer.offer.discountUnit == 0) {
                            var sale = (currentPrice * userOffer.offer.discount / 100)

                            if (userOffer.offer.maxDiscount != -1L && sale >= userOffer.offer.maxDiscount) {
                                // so tien khuyen mai >= so tien duoc phep khuyen mai
                                sale = userOffer.offer.maxDiscount
                                mBinding?.tvSale?.text = sale.toVND()
                            } else {
                                // khong gioi han so tien khuyen mai
                                // so tien < so tien duoc khuyen mai
                                mBinding?.tvSale?.text = sale.toVND()
                            }

                            val totalPrice = currentPrice - sale
                            mBinding?.tvTotalPrice?.text = totalPrice.toVND()
                            mCheckOutViewModel.mTotalPrice = totalPrice
                            strPrice=totalPrice.toString()
                        } else {
                            // tong tien =  so tien - so tien khuyen mai
                            // so tien khuyen mai = so tien khuyen mai
                            mBinding?.tvSale?.text = userOffer.offer.discount.toVND()
                            val totalPrice = (currentPrice - userOffer.offer.discount)
                            mBinding?.tvTotalPrice?.text = totalPrice.toVND()
                            mCheckOutViewModel.mTotalPrice = totalPrice
                            strPrice= totalPrice.toString()
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

    private fun takeASuitableOffer(userOffers: List<UserOffer>?) {
        userOffers?.run {
            val newOffers = userOffers.sortedByDescending { it.offer.maxDiscount }
            newOffers.forEach { userOffer ->
                if (!userOffer.used && userOffer.offer.valueToApply <= mCheckOutViewModel.mPrice) {
                    mCheckOutViewModel.mCurrentOffer.value = userOffer
                    return@forEach
                }
            }
        }

    }

    private fun setupBackBtn() {
        mBinding?.imvNavigationIcon?.setOnClickListener {
            mCheckOutViewModel.clearData()
            mNavController?.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCheckOutViewModel.clearData()
    }


}