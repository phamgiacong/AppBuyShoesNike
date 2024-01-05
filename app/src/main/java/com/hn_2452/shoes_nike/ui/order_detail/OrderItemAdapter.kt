package com.hn_2452.shoes_nike.ui.order_detail

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.OrderDetail
import com.hn_2452.shoes_nike.databinding.SubOrderItemBinding
import com.hn_2452.shoes_nike.utility.dpToPx
import javax.inject.Inject

val OrderDetailUtil = object : DiffUtil.ItemCallback<OrderDetail>() {
    override fun areItemsTheSame(oldItem: OrderDetail, newItem: OrderDetail) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: OrderDetail, newItem: OrderDetail) = oldItem == newItem
}

class OrderItemAdapter @Inject constructor() :
    ListAdapter<OrderDetail, OrderItemAdapterViewHolder>(OrderDetailUtil) {

    var mOnSelect: (OrderDetail) -> Unit = {}
    var mOnReviewOrder: (OrderDetail) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderItemAdapterViewHolder(
            SubOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            mOnSelect,
            mOnReviewOrder
        )

    override fun onBindViewHolder(holder: OrderItemAdapterViewHolder, position: Int) =
        holder.bind(getItem(position))
}

class OrderItemAdapterViewHolder(
    private val mBinding: SubOrderItemBinding,
    private val mOnSelect: (OrderDetail) -> Unit,
    private val mOnReviewOrder: (OrderDetail) -> Unit
) : ViewHolder(mBinding.root) {

    fun bind(orderDetail: OrderDetail) {
        mBinding.root.setPadding(
            dpToPx(mBinding.root.context, 16),
            dpToPx(mBinding.root.context, 16),
            dpToPx(mBinding.root.context, 16),
            dpToPx(mBinding.root.context, 16),
        )
        mBinding.tvShoesName.text = orderDetail.shoes.name
        mBinding.tvShoesQuantity.text =
            mBinding.root.context.getString(R.string.shoes_quantity, orderDetail.quantity)
        mBinding.shoesColor.setCardBackgroundColor(Color.parseColor(orderDetail.color))
        mBinding.shoesSize.text =
            mBinding.root.context.getString(R.string.shoes_size, orderDetail.size)

        if(orderDetail.evaluated == 1 || orderDetail.evaluated == 2) {
            mBinding.evaluated.visibility = View.VISIBLE
            mBinding.evaluated.setOnClickListener {
                mOnReviewOrder(orderDetail)
            }
        } else{
            mBinding.evaluated.visibility = View.GONE
        }


        mBinding.root.setOnClickListener {
            mOnSelect(orderDetail)
        }
    }
}
