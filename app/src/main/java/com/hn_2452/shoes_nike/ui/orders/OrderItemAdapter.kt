package com.hn_2452.shoes_nike.ui.orders

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Order
import com.hn_2452.shoes_nike.databinding.OrderItemBinding
import com.hn_2452.shoes_nike.databinding.SubOrderItemBinding
import com.hn_2452.shoes_nike.utility.toVND
import javax.inject.Inject

val OrderDiff = object : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem

}

class OrderItemAdapter @Inject constructor() :
    ListAdapter<Order, OrderItemAdapter.OrderItemAdapterViewHolder>(OrderDiff) {
    var mOnDetail: (Order) -> Unit = {}

    class OrderItemAdapterViewHolder(
        private val mBinding: OrderItemBinding,
        private val mOnDetail: (Order) -> Unit
    ) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(order: Order) {
            order.orderDetails.forEach { orderDetail ->
                val subOrderBinding =
                    SubOrderItemBinding.inflate(LayoutInflater.from(mBinding.root.context))
                subOrderBinding.tvShoesName.text = orderDetail.shoes.name
                subOrderBinding.tvShoesQuantity.text =
                    mBinding.root.context.getString(R.string.shoes_quantity, orderDetail.quantity)
                subOrderBinding.shoesColor.setCardBackgroundColor(Color.parseColor(orderDetail.color))
                subOrderBinding.shoesSize.text =
                    mBinding.root.context.getString(R.string.shoes_size, orderDetail.size)

                mBinding.orderDetailContainer.addView(subOrderBinding.root)
            }
            mBinding.tvOrderStatus.text = getStatusOfOrder(mBinding.root.context, order.status)
            mBinding.tvTotalPrice.text = order.totalPrice.toVND()
        }

        private fun getStatusOfOrder(context: Context, status: Int): String {
            return when (status) {
                0 -> context.getString(R.string.packing)
                1 -> context.getString(R.string.delevering)
                2 -> context.getString(R.string.shipping)
                3 -> context.getString(R.string.completed)
                4 -> context.getString(R.string.cancel_order)
                else -> ""
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderItemAdapterViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            mOnDetail
        )

    override fun onBindViewHolder(holder: OrderItemAdapterViewHolder, position: Int) =
        holder.bind(getItem(position))
}