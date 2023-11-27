package com.hn_2452.shoes_nike.ui.searching

import android.annotation.SuppressLint
import coil.load
import com.airbnb.epoxy.TypedEpoxyController
import com.hn_2452.shoes_nike.BASE_URL
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Shoes
import com.hn_2452.shoes_nike.databinding.LayoutShoesItemBinding
import com.hn_2452.shoes_nike.utility.ViewBindingModel
import com.hn_2452.shoes_nike.utility.toVND
import javax.inject.Inject

class ShoesAdapterController @Inject constructor() : TypedEpoxyController<List<Shoes>>() {

    private var mOnClick: (shoes: Shoes) -> Unit = {}

    fun setOnClick(onClick: (shoes: Shoes) -> Unit) {
        mOnClick = onClick
    }

    override fun buildModels(data: List<Shoes>?) {
        if (data.isNullOrEmpty()) {
            return
        }

        data.forEach { shoes ->
            ShoesModel(shoes, mOnClick).id(shoes._id).addTo(this)
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
            tvShoesPrice.text = shoes.price.toLong().toVND()
            tvShoesSold.text = "${shoes.sold} đã bán"
            root.setOnClickListener {
                onClick(shoes)
            }
        }

    }

}