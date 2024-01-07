package com.hn_2452.shoes_nike.ui

import android.annotation.SuppressLint
import android.view.View
import coil.load
import com.airbnb.epoxy.TypedEpoxyController
import com.hn_2452.shoes_nike.BASE_URL
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.databinding.LayoutShoesItemBinding
import com.hn_2452.shoes_nike.utility.ViewBindingModel
import com.hn_2452.shoes_nike.utility.getOriginPrice
import com.hn_2452.shoes_nike.utility.getPrice

class ShoesAdapterController(private val onClick: (shoes: Shoes) -> Unit) : TypedEpoxyController<List<Shoes>>() {
    override fun buildModels(data: List<Shoes>?) {
        if (data.isNullOrEmpty()) {
            return
        }

        data.forEach { shoes ->
            ShoesModel(shoes, onClick).id(shoes._id).addTo(this)
        }
    }


    class ShoesModel(
        private val shoes: Shoes,
        private val onClick: (shoes: Shoes) -> Unit
    ) : ViewBindingModel<LayoutShoesItemBinding>(R.layout.layout_shoes_item) {
        @SuppressLint("SetTextI18n")
        override fun LayoutShoesItemBinding.bind() {
            imvShoesImage.load(BASE_URL + shoes.main_image)

            tvShoesName.text = shoes.name
            tvShoesRate.text = shoes.rate.toString()
            tvShoesPrice.text = getPrice(shoes)
            if(shoes.discount > 0) {
                tvShoesOriginPrice.visibility = View.VISIBLE
                tvShoesOriginPrice.text = getOriginPrice(shoes)
            } else {
                tvShoesOriginPrice.visibility = View.GONE
            }
            tvShoesSold.text = "${shoes.sold} đã bán"
            root.setOnClickListener {
                onClick(shoes)
            }
        }

    }

}