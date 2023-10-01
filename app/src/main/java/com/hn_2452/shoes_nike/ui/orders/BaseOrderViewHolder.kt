package com.hn_2452.shoes_nike.ui.orders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.databinding.ViewItemOrderBinding

class BaseOrderViewHolder(binding: ViewItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
    var imgProduct = binding.imgProduct
    var tvNameProduct = binding.tvNameProduct
    var viewColor = binding.viewColor
    var tvNameColor = binding.tvNameColor
    var tvSize = binding.tvSize
    var tvQuantity = binding.tvQuantity
    var tvNameOrderStatus = binding.tvNameOrderStatus
    var tvTotal = binding.tvTotal
    var btnItemOrder = binding.btnItemOrder
}