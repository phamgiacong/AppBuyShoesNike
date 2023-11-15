package com.hn_2452.shoes_nike.ui.home.shoes

import android.view.View
import androidx.core.view.isVisible
import com.airbnb.epoxy.TypedEpoxyController
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.LayoutShoesColorItemBinding
import com.hn_2452.shoes_nike.utility.ViewBindingModel

class ColorAdapter(private val onClick: (String, Boolean) -> Unit) :
    TypedEpoxyController<List<String>>() {
    override fun buildModels(data: List<String>?) {
        data?.forEach { color ->
            ColorModel(color, onClick).id(color).addTo(this)
        }
    }

    class ColorModel(
        private val color: String,
        private val onClick: (String, Boolean) -> Unit
    ) : ViewBindingModel<LayoutShoesColorItemBinding>(R.layout.layout_shoes_color_item) {
        override fun LayoutShoesColorItemBinding.bind() {
            root.setOnClickListener {
                if (imvChecker.isVisible) {
                    imvChecker.visibility = View.INVISIBLE
                    onClick(color, false)
                } else {
                    imvChecker.visibility = View.VISIBLE
                    onClick(color, true)
                }
            }
        }

    }
}