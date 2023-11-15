package com.hn_2452.shoes_nike.ui.home.shoes

import android.graphics.Color
import com.airbnb.epoxy.TypedEpoxyController
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.LayoutShoesSizeItemBinding
import com.hn_2452.shoes_nike.utility.ViewBindingModel

class SizeAdapter(private val onClick: (Int) -> Unit) : TypedEpoxyController<List<Int>>() {
    override fun buildModels(data: List<Int>?) {
        data?.forEach {
            SizeModel(it, onClick).id(it).addTo(this)
        }
    }

    class SizeModel(
        private val size: Int,
        private val onClick: (Int) -> Unit
    ) : ViewBindingModel<LayoutShoesSizeItemBinding>(R.layout.layout_shoes_size_item) {
        override fun LayoutShoesSizeItemBinding.bind() {
            tvSize.text = size.toString()
            root.setOnClickListener {
                root.setBackgroundColor(Color.BLACK)
                tvSize.setTextColor(Color.WHITE)
                onClick(size)
            }
        }

    }


}