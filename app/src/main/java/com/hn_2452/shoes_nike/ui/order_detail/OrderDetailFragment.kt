package com.hn_2452.shoes_nike.ui.order_detail

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.databinding.FragmentOrderDetailBinding
import com.hn_2452.shoes_nike.databinding.LayoutCancelOrderBinding
import com.hn_2452.shoes_nike.databinding.LayoutEvaluateOrderBinding
import com.hn_2452.shoes_nike.utility.handleResource
import com.hn_2452.shoes_nike.utility.toVND
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>() {

    companion object {
        const val TAG = "Nike:OrderDetailFragment: "
    }

    private val mAgrs: OrderDetailFragmentArgs by navArgs()

    private val mOrderDetailViewModel: OrderDetailViewModel by viewModels()

    @Inject
    lateinit var mOrderItemAdapter: OrderItemAdapter

    lateinit var mCancelOrderBtSheet: BottomSheetDialog

    lateinit var mReviewFormBinding: LayoutEvaluateOrderBinding
    lateinit var mReviewForm: BottomSheetDialog

    private lateinit var mPrimaryColor: ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPrimaryColor = ColorStateList.valueOf(
            this@OrderDetailFragment.resources.getColor(
                R.color.primary_color,
                null
            )
        )
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOrderDetailBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.toolBar)
        setupLoading(mBinding?.progressBar)
        setupRcvOrderItem()
        loadOrder()
        setupCancelOrder()
        setupCancelOrderBtSheet()
        setupReviewForm()
    }

    private fun setupReviewForm() {
        mReviewFormBinding =
            LayoutEvaluateOrderBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        mReviewForm = BottomSheetDialog(requireContext())
        mReviewForm.setContentView(mReviewFormBinding.root)
        with(mReviewFormBinding) {
            btnClose.setOnClickListener { mReviewForm.dismiss() }
        }
    }

    private fun setupCancelOrderBtSheet() {
        val binding =
            LayoutCancelOrderBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        mCancelOrderBtSheet = BottomSheetDialog(requireContext())
        mCancelOrderBtSheet.setContentView(binding.root)

        with(binding) {
            btnRemove.setOnClickListener {

                var reason = ""
                val selectedId: Int = binding.radio.checkedRadioButtonId
                if (selectedId == -1) {
                    Toast.makeText(
                        requireContext(),
                        getText(R.string.request_select_cancel_reason),
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                when (selectedId) {
                    R.id.wrong_time -> {
                        reason = getString(R.string.wrong_tịme)
                    }

                    R.id.wrong_intent -> {
                        reason = getString(R.string.wrong_intent)
                    }

                    R.id.wrong_order -> {
                        reason = getString(R.string.wrong_order)
                    }

                    R.id.wrong_address -> {
                        reason = getString(R.string.wrong_address)
                    }

                    R.id.wrong_other -> {
                        reason = getString(R.string.wrong_other)
                    }
                }

                reason += "(${binding.edtMoreReason.editText?.text.toString()})."


                handleResource(
                    data = mOrderDetailViewModel.cancelOrder(mAgrs.id, reason),
                    lifecycleOwner = viewLifecycleOwner,
                    context = requireContext(),
                    isErrorInform = true,
                    onSuccess = {
                        mCancelOrderBtSheet.dismiss()
                        loadOrder()
                    }
                )
            }

            btnClose.setOnClickListener { mCancelOrderBtSheet.dismiss() }
        }
    }

    private fun setupCancelOrder() {
        mBinding?.cancelOrder?.setOnClickListener {
            mCancelOrderBtSheet.show()
        }
    }

    private fun setupRcvOrderItem() {
        mOrderItemAdapter.mOnSelect = { orderDetail ->
            mNavController?.navigate(
                OrderDetailFragmentDirections.actionOrderDetailFragmentToShoesFragment(orderDetail.shoes._id)
            )
        }
        mOrderItemAdapter.mOnReviewOrder = { orderDetail ->
            showReviewForm(orderDetail)
        }
        mBinding?.rcvOrderItems?.adapter = mOrderItemAdapter
    }

    private fun showReviewForm(orderDetail: OrderDetail) {
        with(mReviewFormBinding) {
            orderItem.imgProduct.load(orderDetail.shoes.main_image)
            orderItem.tvNameProduct.text = orderDetail.shoes.name
            orderItem.cavColor.setCardBackgroundColor(Color.parseColor(orderDetail.color))
            orderItem.tvSize.text = getString(R.string.shoes_size, orderDetail.size)
            orderItem.tvQuantity.text = getString(R.string.shoes_quantity, orderDetail.quantity)
            orderItem.tvPriceProduct.visibility = View.INVISIBLE
            btnConfirm.setOnClickListener {
                val star = mReviewFormBinding.star.rating
                val comment = mReviewFormBinding.edtComment.editText?.text.toString()

                if (comment.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_enter_your_comment), Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                handleResource(
                    data = mOrderDetailViewModel.reviewShoes(
                        orderDetail.id,
                        orderDetail.shoes._id,
                        star,
                        comment
                    ),
                    lifecycleOwner = viewLifecycleOwner,
                    context = requireContext(),
                    isErrorInform = true,
                    onSuccess = {
                        loadOrder()
                        Toast.makeText(requireContext(), "Đã đánh giá đơn hàng", Toast.LENGTH_SHORT)
                            .show()
                        mReviewForm.dismiss()
                    }
                )
            }
        }
        mReviewForm.show()
    }

    private fun loadOrder() {
        Log.i(TAG, "loadOrder: " + mAgrs.id)
        handleResource(
            data = mOrderDetailViewModel.getOrderDetailById(mAgrs.id),
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            isErrorInform = true,
            onLoading = { stopLoading() },
            onError = { stopLoading() },
            onSuccess = { order ->
                order?.run {
                    mOrderItemAdapter.submitList(orderDetails)
                    mBinding?.tvAddressTitle?.visibility = View.VISIBLE
                    mBinding?.tvName?.text = address?.name
                    mBinding?.tvPhoneNumber?.text = address?.phoneNumber
                    mBinding?.tvAddress?.text = address?.address
                    mBinding?.tvTotalPrice?.text =
                        getString(R.string.order_detail_total_price, totalPrice.toVND())

                    when (status) {
                        4 -> {
                            mBinding?.tvCancelReason?.text =
                                getString(R.string.cancel_reason, cancelReason)
                            mBinding?.layoutInactive?.visibility = View.VISIBLE
                            mBinding?.layoutActive?.visibility = View.GONE
                        }

                        3 -> {
                            mBinding?.run {
                                pack.imageTintList = mPrimaryColor
                                packTicker.imageTintList = mPrimaryColor
                                line1.imageTintList = mPrimaryColor
                                transport.imageTintList = mPrimaryColor
                                transportTick.imageTintList = mPrimaryColor
                                line2.imageTintList = mPrimaryColor
                                delivery.imageTintList = mPrimaryColor
                                deliveryTick.imageTintList = mPrimaryColor
                                line3.imageTintList = mPrimaryColor
                                upPack.imageTintList = mPrimaryColor
                                upPackTick.imageTintList = mPrimaryColor
                                layoutActive.visibility = View.VISIBLE
                                cancelOrder.visibility = View.GONE
                            }
                            mBinding?.layoutCompleted?.visibility = View.VISIBLE
                        }

                        0 -> {
                            mBinding?.run {
                                pack.imageTintList = mPrimaryColor
                                packTicker.imageTintList = mPrimaryColor
                                layoutActive.visibility = View.VISIBLE
                            }
                        }

                        1 -> {
                            mBinding?.run {
                                pack.imageTintList = mPrimaryColor
                                packTicker.imageTintList = mPrimaryColor
                                line1.imageTintList = mPrimaryColor
                                transport.imageTintList = mPrimaryColor
                                transportTick.imageTintList = mPrimaryColor
                                layoutActive.visibility = View.VISIBLE
                            }

                        }

                        2 -> {
                            mBinding?.run {
                                pack.imageTintList = mPrimaryColor
                                packTicker.imageTintList = mPrimaryColor
                                line1.imageTintList = mPrimaryColor
                                transport.imageTintList = mPrimaryColor
                                transportTick.imageTintList = mPrimaryColor
                                line2.imageTintList = mPrimaryColor
                                delivery.imageTintList = mPrimaryColor
                                deliveryTick.imageTintList = mPrimaryColor
                                layoutActive.visibility = View.VISIBLE
                                tvCancelReason.visibility = View.GONE
                            }
                        }

                        -1 -> {
                            mBinding?.status?.text = getString(R.string.not_yet_confirm)
                            mBinding?.status?.visibility = View.VISIBLE
                            mBinding?.layoutActive?.visibility = View.VISIBLE
                        }
                    }
                }
            }
        )
    }


}