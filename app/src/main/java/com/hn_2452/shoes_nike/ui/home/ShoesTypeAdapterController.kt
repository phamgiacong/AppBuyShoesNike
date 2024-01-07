package com.hn_2452.shoes_nike.ui.home

import com.airbnb.epoxy.TypedEpoxyController
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.ShoesType
import com.hn_2452.shoes_nike.databinding.LayoutShoesTypeItemBinding
import com.hn_2452.shoes_nike.utility.ViewBindingModel

class ShoesTypeAdapterController(private val mOnClick: (ShoesType) -> Unit) : TypedEpoxyController<List<ShoesType>>() {
    override fun buildModels(shoesTypes: List<ShoesType>?) {
        shoesTypes?.let {
            shoesTypes.forEach { shoesType ->
                ShoesTypeModel(shoesType, mOnClick).id(shoesType.id).addTo(this)
            }
        }
    }
    class ShoesTypeModel(
        private val shoesType: ShoesType,
        private val mOnClick: (ShoesType) -> Unit
    ) : ViewBindingModel<LayoutShoesTypeItemBinding>(R.layout.layout_shoes_type_item) {
        override fun LayoutShoesTypeItemBinding.bind() {
            tvShoesTypeName.text = shoesType.name
            tvShoesTypeName.setOnClickListener {
                mOnClick(shoesType)
            }
        }

    }


}