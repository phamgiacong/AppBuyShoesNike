package com.hn_2452.shoes_nike.ui.home.adapter

import com.airbnb.epoxy.TypedEpoxyController
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.ShoesType
import com.hn_2452.shoes_nike.databinding.LayoutShoesTypeItemBinding
import com.hn_2452.shoes_nike.utility.ViewBindingModel

class ShoesTypeAdapterController : TypedEpoxyController<List<ShoesType>>() {
    override fun buildModels(shoesTypes: List<ShoesType>?) {
        shoesTypes?.let {
            shoesTypes.forEach { shoesType ->
                ShoesTypeModel(shoesType).id(shoesType.id).addTo(this)
            }
        }
    }
    class ShoesTypeModel(
        private val shoesType: ShoesType
    ) : ViewBindingModel<LayoutShoesTypeItemBinding>(R.layout.layout_shoes_type_item) {
        override fun LayoutShoesTypeItemBinding.bind() {
            tvShoesTypeName.text = shoesType.name
        }

    }


}