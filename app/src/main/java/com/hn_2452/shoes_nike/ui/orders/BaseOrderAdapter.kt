package com.hn_2452.shoes_nike.ui.orders

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.OrderModel
import com.hn_2452.shoes_nike.databinding.ViewItemOrderBinding
import com.hn_2452.shoes_nike.extension.setSafeOnClickListener

class BaseOrderAdapter(

    private var list: List<OrderModel>,
    private var context: Context,
    private var onClick: OnClick

) : RecyclerView.Adapter<BaseOrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseOrderViewHolder {
        val binding =
            ViewItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseOrderViewHolder, position: Int) {
        val currentItem = list[position]

        Glide.with(context)
            .load(currentItem.imageProduct)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.imgProduct)
        holder.tvNameProduct.text = currentItem.nameProduct
        holder.viewColor.background.setTint(Color.parseColor(currentItem.colorProduct))
        holder.tvNameColor.text = currentItem.colorProduct
        holder.tvSize.text = "Size = ${currentItem.sizeProduct}"
        holder.tvQuantity.text = "Qty = ${currentItem.quantity}"
        holder.tvTotal.text = "$${currentItem.total}"

        holder.btnItemOrder.text = "Track Order"

        if (currentItem.orderPlaced) {
            holder.tvNameOrderStatus.text = "Wait for Confirmation"
        } else if (currentItem.orderConfirmed) {
            holder.tvNameOrderStatus.text = "In Delivery"
        } else if (currentItem.orderShipped) {
            holder.tvNameOrderStatus.text = "Out for Delivery"
        } else if (currentItem.outForDelivery) {
            holder.tvNameOrderStatus.text = "Delivered"
        } else if (currentItem.orderDelivered) {
            holder.tvNameOrderStatus.text = "Completed"
            holder.btnItemOrder.text = "Buy Again"
        }

        holder.btnItemOrder.setSafeOnClickListener {
            onClick.detail(currentItem)
        }

    }

    interface OnClick {
        fun detail(orderModel: OrderModel)
    }
}