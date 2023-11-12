package com.hn_2452.shoes_nike.ui.home.adapter

import coil.load
import com.airbnb.epoxy.TypedEpoxyController
import com.hn_2452.shoes_nike.BASE_URL
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.databinding.LayoutShoesItemBinding
import com.hn_2452.shoes_nike.utility.ViewBindingModel

class ShoesAdapterController : TypedEpoxyController<List<Shoes>>() {
    override fun buildModels(data: List<Shoes>?) {
        if (data.isNullOrEmpty()) {
            return
        }

        data.forEach { shoes ->
            ShoesModel(shoes).id(shoes.id).addTo(this)
        }
    }

    class ShoesModel(
        private val shoes: Shoes
    ) : ViewBindingModel<LayoutShoesItemBinding>(R.layout.layout_shoes_item) {
        override fun LayoutShoesItemBinding.bind() {
            imvShoesImage.load(BASE_URL + shoes.mainImage)
            tvShoesName.text = shoes.name
            tvShoesRate.text = shoes.rate.toString()
            tvShoesPrice.text = shoes.price.toString()
            tvShoesSold.text = "${shoes.sold} sold"
        }

    }

}