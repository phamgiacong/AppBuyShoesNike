package com.hn_2452.shoes_nike.ui.cart.check_out

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.zalopaykotlin.Api.CreateOrder
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.data.model.UserOffer
import com.hn_2452.shoes_nike.databinding.FragmentCheckOutBinding
import com.hn_2452.shoes_nike.utility.handleResource
import com.hn_2452.shoes_nike.utility.toDayString
import com.hn_2452.shoes_nike.utility.toVND
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import javax.inject.Inject

@AndroidEntryPoint
class CheckOutFragment : BaseFragment<FragmentCheckOutBinding>() {
    private var totalPrice: String? = null

    companion object {
        const val TAG = "Nike:CheckOutFragment: "
    }

    private val mCheckOutViewModel: CheckOutViewModel by activityViewModels()

    private val mArg: CheckOutFragmentArgs by navArgs()

    @Inject
    lateinit var mOrderItemAdapter: OrderItemAdapter


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCheckOutBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        ZaloPaySDK.init(553, Environment.SANDBOX)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")
        setupBackBtn()
        setupSelectAddress()
        setupAddressData()
        setupPaymentMethod()
        setupOrderButton()
        setupOrderList()
        setupOffer()
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
                        mCheckOutViewModel.clearData()
                        mNavController?.navigate(
                            CheckOutFragmentDirections.actionCheckOutFragmentToHomeFragment()
                        )
                    },
                    isErrorInform = true
                )
            } else {
                val orderApi = CreateOrder()
                val data: JSONObject? = totalPrice?.let { it1 -> orderApi.createOrder(it1) }
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
                                                        "TransactionId: %s - TransToken: %s",
                                                        transactionId,
                                                        transToken
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
                                                            Toast.makeText(
                                                                requireContext(),
                                                                "Đặt đơn hàng thành công",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                            mNavController?.navigate(
                                                                CheckOutFragmentDirections.actionCheckOutFragmentToHomeFragment()
                                                            )
                                                        },
                                                        isErrorInform = true
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
                                                    "zpTransToken: %s \n",
                                                    zpTransToken
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
                                                    "ZaloPayErrorCode: %s \nTransToken: %s",
                                                    zaloPayError.toString(),
                                                    zpTransToken
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
                        } else {
                            // tong tien =  so tien - so tien khuyen mai
                            // so tien khuyen mai = so tien khuyen mai
                            mBinding?.tvSale?.text = userOffer.offer.discount.toVND()
                            val totalPrice = (currentPrice - userOffer.offer.discount)
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
        val cartItemIds: Array<String> = mArg.getCartItem()
        val cartItemIdList = cartItemIds.toList()
        handleResource(
            data = mCheckOutViewModel.getCartItemOfUser(cartItemIdList),
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
                mCheckOutViewModel.mTotalPrice = currentPrice
                mBinding?.tvAmount?.text = currentPrice.toVND()
                mBinding?.tvTotalPrice?.text = currentPrice.toVND()
                totalPrice = currentPrice.toString()
                val currentOffer = mCheckOutViewModel.mCurrentOffer.value
                if (currentOffer != null) {
                    if (currentOffer.offer.discountUnit == 0) {
                        var sale = (currentPrice * currentOffer.offer.discount / 100)

                        if (currentOffer.offer.maxDiscount != -1L && sale >= currentOffer.offer.maxDiscount) {
                            // so tien khuyen mai >= so tien duoc phep khuyen mai
                            sale = currentOffer.offer.maxDiscount
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
                        mBinding?.tvSale?.text = currentOffer.offer.discount.toVND()
                        val totalPrice = (currentPrice - currentOffer.offer.discount)
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

    private fun takeASuitableOffer(offers: List<UserOffer>?) {
        offers?.run {
            val newOffers = offers.sortedByDescending { it.offer.maxDiscount }
            newOffers.forEach { offer ->
                if (!offer.used && offer.offer.valueToApply <= mCheckOutViewModel.mPrice) {
                    mCheckOutViewModel.mCurrentOffer.value = offer
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