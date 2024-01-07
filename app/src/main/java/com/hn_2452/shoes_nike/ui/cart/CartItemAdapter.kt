package com.hn_2452.shoes_nike.ui.cart

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.hn_2452.shoes_nike.BASE_URL
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.databinding.ItemCartBinding
import com.hn_2452.shoes_nike.utility.getOriginPrice
import com.hn_2452.shoes_nike.utility.getPrice
import com.hn_2452.shoes_nike.utility.toVND
import javax.inject.Inject

val OrderDetailUtil = object : DiffUtil.ItemCallback<OrderDetail>() {
    override fun areItemsTheSame(oldItem: OrderDetail, newItem: OrderDetail) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: OrderDetail, newItem: OrderDetail) = oldItem == newItem
}

class CartItemAdapter @Inject constructor() :
    ListAdapter<OrderDetail, CartItemAdapterHolder>(OrderDetailUtil) {

    var mOnSelectItem: (shoesId: String) -> Unit = {}
    var mOnDeleteItem: (cartItem: OrderDetail) -> Unit = {}
    var mOnIncreaseQuantity: (cartItemId: String, updatedQuantity: Int) -> Boolean = { _, _ -> false}
    var mOnReduceQuantity: (cartItemId: String, reducedQuantity: Int) -> Boolean = { _, _ -> false}
    var mOnCheck: (OrderDetail, Boolean) -> Unit = {_, _ ->}

    var mPreviousOrderDetailIds : List<String>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CartItemAdapterHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            mOnSelectItem,
            mOnDeleteItem,
            mOnIncreaseQuantity,
            mOnReduceQuantity,
            mOnCheck,
            mPreviousOrderDetailIds
        )

    override fun onBindViewHolder(holder: CartItemAdapterHolder, position: Int) =
        holder.bind(getItem(position))

}

class CartItemAdapterHolder(
    private val mBinding: ItemCartBinding,
    private val mOnSelectItem: (shoesId: String) -> Unit,
    private val mOnDeleteItem: (cartItem: OrderDetail) -> Unit,
    private val mOnIncreaseQuantity: (cartId: String, increasedQuantity: Int) -> Boolean,
    private val mOnReduceQuantity: (cartId: String, reducedQuantity: Int) -> Boolean,
    private val mOnCheck: (OrderDetail, Boolean) -> Unit,
    private val mPreviousOrderDetailIds: List<String>?
) : ViewHolder(mBinding.root) {

    fun bind(cartItem: OrderDetail) {
        with(mBinding) {
            imgProduct.load( BASE_URL + cartItem.shoes.main_image) {
                error(R.drawable.image_broken)
            }

            tvNameProduct.text = cartItem.shoes.name
            tvPriceProduct.text = getPrice(cartItem.shoes)
            if(cartItem.shoes.discount > 0) {
                tvOriginPrice.visibility = View.VISIBLE
                tvOriginPrice.text = getOriginPrice(cartItem.shoes)
            } else {
                tvOriginPrice.visibility = View.GONE
            }
            tvSize.text = "Cỡ: ${cartItem.size}"
            tvQuantity.text = cartItem.quantity.toString()
            cavColor.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(cartItem.color)))

            root.setOnClickListener { mOnSelectItem(cartItem.shoes._id) }
            removeItem.setOnClickListener { mOnDeleteItem(cartItem) }
            btnReduce.setOnClickListener {
                val reducedQuantity = cartItem.quantity - 1
                val result = mOnReduceQuantity(cartItem.id, reducedQuantity)
                if(result) {
                    tvQuantity.text = reducedQuantity.toString()
                }
            }
            btnAugment.setOnClickListener {
                val increasedQuantity = cartItem.quantity + 1
                val result = mOnIncreaseQuantity(cartItem.id, increasedQuantity)
                if(result) {
                    tvQuantity.text = increasedQuantity.toString()
                }
            }

            if(mPreviousOrderDetailIds == null) {
                checkBox.isChecked = true
            } else {
                checkBox.isChecked = mPreviousOrderDetailIds.contains(cartItem.id)
            }

            checkBox.setOnClickListener {
                mOnCheck(cartItem, checkBox.isChecked)
            }

        }

    }
}