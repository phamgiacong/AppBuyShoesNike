package com.hn_2452.shoes_nike.ui.cart

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUser()
        setupBottomBar(true)
        setupLoading(mBinding?.loadingProgress)
    }

    private fun setupUser() {
        mCartViewModel.mCurrentUser.observe(viewLifecycleOwner) {
            if(it != null && it.isNotEmpty()) {
                mBinding?.layoutNeedLogin?.visibility = View.GONE
                mBinding?.mainLayout?.visibility = View.VISIBLE
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
            if(mCartViewModel.mAvailableToCheckout) {
                mNavController?.navigate(R.id.checkOutFragment)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        setupBottomBar(false)
    }
    private fun setupCartOfUser() {
        mCartItemAdapter.mOnSelectItem = { shoesId -> viewShoes(shoesId) }
        mCartItemAdapter.mOnDeleteItem = { cartItem -> deleteCartItem(cartItem) }
        mCartItemAdapter.mOnIncreaseQuantity =
            { cartItemId, updatedQuantity -> increaseQuantity(cartItemId, updatedQuantity) }
        mCartItemAdapter.mOnReduceQuantity =
            { cartItemId, reducedQuantity -> reduceQuantity(cartItemId, reducedQuantity) }
        mBinding?.rcvCartItem?.adapter = mCartItemAdapter
        loadCartOfUser()
    }

    private fun loadCartOfUser() {
        handleResource(
            data = mCartViewModel.getCartOfUser(),
            lifecycleOwner = viewLifecycleOwner,
            onLoading = { startLoading() },
            isErrorInform = true,
            onError = { stopLoading() },
            onSuccess = { cartItemList ->
                stopLoading()
                mCartViewModel.mAvailableToCheckout = cartItemList?.isNotEmpty() ?: false
                mCartItemAdapter.submitList(cartItemList ?: emptyList())
                updateTotalPrice(cartItemList)
            },
            context = requireContext()
        )
    }

    private fun updateTotalPrice(cartItemList: List<OrderDetail>?) {
        var price = 0L
        cartItemList?.forEach { item ->
            price += item.quantity * item.shoes.price.toLong()
        }
        mBinding?.tvPrice?.text = price.toVND()
    }


    private fun viewShoes(shoesId: String) {
        mNavController?.navigate(
            CartFragmentDirections.actionCartFragmentToShoesFragment(shoesId)
        )
    }

    private fun deleteCartItem(cartItem: OrderDetail) {
        val binding =
            LayoutRemoveCartItemBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(binding.root)

        with(binding) {
            imgProduct.load(cartItem.shoes.main_image) {
                error(R.drawable.ic_launcher_background)
            }

            tvNameProduct.text = cartItem.shoes.name
            tvPriceProduct.text = cartItem.shoes.price.toLong().toVND()
            tvSize.text = "Cỡ: ${cartItem.size}"
            tvQuantity.text = cartItem.quantity.toString()
            cavColor.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(cartItem.color)))

            btnClose.setOnClickListener { bottomSheet.dismiss() }
            btnRemove.setOnClickListener {
                deleteCartItem(cartItem.id)
                bottomSheet.dismiss()
            }
        }
        bottomSheet.show()
    }

    private fun deleteCartItem(cartItem: String) {
        handleResource(
            data = mCartViewModel.deleteCartItem(cartItem),
            lifecycleOwner = viewLifecycleOwner,
            onSuccess = { loadCartOfUser() },
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
        handleResource(
            data = mCartViewModel.updateCartItem(cartItemId, reducedQuantity),
            lifecycleOwner = viewLifecycleOwner,
            onSuccess = { loadCartOfUser() },
            context = requireContext()
        )
        return true
    }


}