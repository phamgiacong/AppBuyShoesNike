package com.hn_2452.shoes_nike.ui.cart.offer

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.PrimaryKey
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.databinding.ItemPromoBinding
import com.hn_2452.shoes_nike.utility.toDayString
import com.hn_2452.shoes_nike.utility.toVND
import javax.inject.Inject


val OfferDiff = object : DiffUtil.ItemCallback<Offer>() {
    override fun areItemsTheSame(oldItem: Offer, newItem: Offer) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Offer, newItem: Offer) = oldItem == newItem
}

class OfferAdapter @Inject constructor() : ListAdapter<Offer, OfferViewHolder>(OfferDiff) {

    var mOnClick: (Offer) -> Unit = {}
    var mOnDetail: (Offer) -> Unit = {}
    var mPrice : Long = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OfferViewHolder(
            ItemPromoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mOnClick, mOnDetail, mPrice
        )

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) =
        holder.bind(getItem(position))

}

class OfferViewHolder(
    private val mBinding: ItemPromoBinding,
    private val mOnClick: (Offer) -> Unit,
    private val mOnDetail: (Offer) -> Unit,
    private val mApplyPrice: Long = 0
) : RecyclerView.ViewHolder(mBinding.root) {

    fun bind(offer: Offer) {
        if(offer.valueToApply > mApplyPrice) {
            mBinding.root.setCardBackgroundColor(Color.parseColor("#DDDDDD"))
        } else {
            mBinding.root.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        }

        val sale = if (offer.discountUnit == 0) {
            "Giảm giá ${offer.discount}%"
        } else {
            "Giảm giá ${offer.discount.toVND()}"
        }

        val saleMore = if (offer.maxDiscount != -1L) {
            "Giảm tối đa ${offer.maxDiscount.toVND()} cho đơn hàng từ ${offer.valueToApply.toVND()}"
        } else {
            "Áp dụng cho đơn hàng từ ${offer.valueToApply.toVND()}"
        }

        mBinding.tvSale.text = sale
        mBinding.tvSaleMore.text = saleMore
        mBinding.tvTitle.text = offer.title
        mBinding.tvExpired.text = "Hết hạn ${offer.endTime.toDayString()}"
        mBinding.tvDetail.setOnClickListener {
            mOnDetail(offer)
        }
        mBinding.root.setOnClickListener {
            mOnClick(offer)
        }
    }


}
