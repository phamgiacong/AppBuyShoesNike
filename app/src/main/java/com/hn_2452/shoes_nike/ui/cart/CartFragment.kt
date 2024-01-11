package com.hn_2452.shoes_nike.ui.cart

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.databinding.FragmentCartBinding
import com.hn_2452.shoes_nike.databinding.LayoutRemoveCartItemBinding
import com.hn_2452.shoes_nike.utility.handleResource
import com.hn_2452.shoes_nike.utility.toVND
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>() {

    companion object {
        const val TAG = "Nike:CartFragment: "
    }

    private val mCartViewModel: CartViewModel by viewModels()

    @Inject
    lateinit var mCartItemAdapter: CartItemAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCartBinding.inflate(inflater, container, false)

    override fun onStart() {
        super.onStart()
        setupBottomBar(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding?.constraintLayout4?.visibility = View.GONE
        setupUser()
        setupBottomBar(true)
        setupLoading(mBinding?.loadingProgress)
    }

    private fun setupUser() {
        mCartViewModel.mCurrentUser.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                mBinding?.layoutNeedLogin?.visibility = View.GONE
                setupCartOfUser()
                setupCheckout()
            } else {
                mBinding?.mainLayout?.visibility = View.GONE
                mBinding?.layoutNeedLogin?.visibility = View.VISIBLE
                mBinding?.btnSignIn?.setOnClickListener {
                    mNavController?.navigate(R.id.loginFragment)
                }
            }
        }
    }

    private fun setupCheckout() {
        mBinding?.btnCheckOut?.setOnClickListener {
            if (mCartViewModel.mSelectedCartItemIdList?.isNotEmpty() == true) {
                mNavController?.navigate(
                    CartFragmentDirections.actionCartFragmentToCheckOutFragment(mCartViewModel.mSelectedCartItemIdList!!.toTypedArray())
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        setupBottomBar(false)
    }

    private fun setupCartOfUser() {
        mCartItemAdapter.mOnSelectItem = { shoesId -> viewShoes(shoesId) }
        mCartItemAdapter.mOnDeleteItem = { cartItem -> showDeleteCartItemPopup(cartItem) }
        mCartItemAdapter.mOnIncreaseQuantity =
            { cartItemId, updatedQuantity -> increaseQuantity(cartItemId, updatedQuantity) }
        mCartItemAdapter.mOnReduceQuantity =
            { cartItemId, reducedQuantity -> reduceQuantity(cartItemId, reducedQuantity) }
        mCartItemAdapter.mOnCheck = { cartItem, checked ->
            handleSelectItem(cartItem, checked)
        }
        mBinding?.rcvCartItem?.adapter = mCartItemAdapter
        loadCartOfUser()
    }

    private fun handleSelectItem(cartItem: OrderDetail, selected: Boolean) {
        if (selected) {
            if (mCartViewModel.mSelectedCartItemIdList?.contains(cartItem.id) == false) {
                mCartViewModel.mSelectedCartItemIdList?.add(cartItem.id)
            }
        } else {
            mCartViewModel.mSelectedCartItemIdList?.remove(cartItem.id)
        }
        updateTotalPrice(getSelectedCartItemList())
    }

    private fun loadCartOfUser() {
        Log.i(TAG, "loadCartOfUser: ")
        handleResource(
            data = mCartViewModel.getCartOfUser(),
            lifecycleOwner = viewLifecycleOwner,
            onLoading = { startLoading() },
            isErrorInform = false,
            onError = { stopLoading() },
            onSuccess = { cartItemList ->
                stopLoading()
                mCartViewModel.mCartItemList = cartItemList?.toMutableList() ?: mutableListOf()
                if (cartItemList.isNullOrEmpty()) {
                    mBinding?.rcvCartItem?.visibility = View.GONE
                    mBinding?.constraintLayout4?.visibility = View.GONE
                    mBinding?.noneItem?.visibility = View.VISIBLE
                } else {
                    mBinding?.rcvCartItem?.visibility = View.VISIBLE
                    mBinding?.constraintLayout4?.visibility = View.VISIBLE
                    if (mBinding?.noneItem?.visibility == View.VISIBLE) {
                        mBinding?.noneItem?.visibility = View.GONE
                    }
                    if (mBinding?.mainLayout?.visibility == View.GONE) {
                        mBinding?.mainLayout?.visibility = View.VISIBLE
                    }

                    if (mCartViewModel.mSelectedCartItemIdList == null) {
                        val selectedCartItemIds = mutableListOf<String>()
                        mCartViewModel.mCartItemList.forEach {
                            selectedCartItemIds.add(it.id)
                        }
                        mCartViewModel.mSelectedCartItemIdList = selectedCartItemIds
                        updateTotalPrice(getSelectedCartItemList())
                    } else {
                        mCartItemAdapter.mPreviousOrderDetailIds = mCartViewModel.mSelectedCartItemIdList!!
                        updateTotalPrice(getSelectedCartItemList())
                    }
                    mCartItemAdapter.submitList(cartItemList)
                }
            },
            context = requireContext()
        )
    }

    private fun getSelectedCartItemList() : List<OrderDetail> {
        val selectedCartItemList = mCartViewModel.mCartItemList.filter {
            mCartViewModel.mSelectedCartItemIdList?.contains(it.id) ?: false
        }
        return selectedCartItemList
    }

    private fun updateTotalPrice(cartItemList: List<OrderDetail>?) {
        var price = 0L
        cartItemList?.forEach { item ->
            price += if (item.shoes.discountUnit == 0) {
                item.quantity * (item.shoes.price - (item.shoes.price * item.shoes.discount / 100))
            } else {
                item.quantity * (item.shoes.price - item.shoes.discount)
            }
        }
        mBinding?.tvPrice?.text = price.toVND()
    }


    private fun viewShoes(shoesId: String) {
        mNavController?.navigate(
            CartFragmentDirections.actionCartFragmentToShoesFragment(shoesId)
        )
    }

    private fun showDeleteCartItemPopup(cartItem: OrderDetail) {
        val binding =
            LayoutRemoveCartItemBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(binding.root)

        with(binding) {
            imgProduct.load(cartItem.shoes.main_image) {
                error(R.color.background_secondary_color)
                placeholder(R.color.background_secondary_color)
            }

            tvNameProduct.text = cartItem.shoes.name
            tvPriceProduct.text = cartItem.shoes.price.toVND()
            tvSize.text = "Cỡ: ${cartItem.size}"
            tvQuantity.text = cartItem.quantity.toString()
            cavColor.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(cartItem.color)))

            btnClose.setOnClickListener { bottomSheet.dismiss() }
            btnRemove.setOnClickListener {
                deleteCartItem(cartItem)
                bottomSheet.dismiss()
            }
        }
        bottomSheet.show()
    }

    private fun deleteCartItem(cartItem: OrderDetail) {
        handleResource(
            data = mCartViewModel.deleteCartItem(cartItem.id),
            lifecycleOwner = viewLifecycleOwner,
            onSuccess = {
                handleSelectItem(cartItem, false)
                loadCartOfUser()
            },
            context = requireContext()
        )
    }


    private fun increaseQuantity(cartItemId: String, increasedQuantity: Int): Boolean {
        handleResource(
            data = mCartViewModel.updateCartItem(cartItemId, increasedQuantity),
            lifecycleOwner = viewLifecycleOwner,
            onSuccess = { loadCartOfUser() },
            context = requireContext()
        )
        return true
    }

    private fun reduceQuantity(cartItemId: String, reducedQuantity: Int): Boolean {
        if (reducedQuantity < 1) {
            return false
        }
        handleResource(
            data = mCartViewModel.updateCartItem(cartItemId, reducedQuantity),
            lifecycleOwner = viewLifecycleOwner,
            onSuccess = { loadCartOfUser() },
            context = requireContext()
        )
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
        mCartViewModel.mSelectedCartItemIdList = null
    }


}