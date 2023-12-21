package com.hn_2452.shoes_nike.ui.cart.check_out

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.databinding.LayoutOrderItemBinding
import com.hn_2452.shoes_nike.utility.toVND
import javax.inject.Inject

val OrderDetailUtil = object : DiffUtil.ItemCallback<OrderDetail>() {
    override fun areItemsTheSame(oldItem: OrderDetail, newItem: OrderDetail) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: OrderDetail, newItem: OrderDetail) = oldItem == newItem
}

class OrderItemAdapter @Inject constructor() :
    ListAdapter<OrderDetail, OrderItemAdapterHolder>(OrderDetailUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderItemAdapterHolder(
            LayoutOrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: OrderItemAdapterHolder, position: Int) =
        holder.bind(getItem(position))

}

class OrderItemAdapterHolder(
    private val mBinding: LayoutOrderItemBinding,
) : ViewHolder(mBinding.root) {
    fun bind(orderItem: OrderDetail) {
        with(mBinding) {
            imgProduct.load(orderItem.shoes.main_image) {
                error(R.drawable.ic_launcher_background)
            }

            tvNameProduct.text = orderItem.shoes.name
            tvPriceProduct.text = orderItem.shoes.price.toLong().toVND()
            tvSize.text = "Cỡ: ${orderItem.size}"
            tvQuantity.text = "Số lượng: ${orderItem.quantity}"
            cavColor.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(orderItem.color)))
        }

    }
}