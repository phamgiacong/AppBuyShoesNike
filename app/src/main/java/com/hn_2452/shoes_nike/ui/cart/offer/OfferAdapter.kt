package com.hn_2452.shoes_nike.ui.cart.offer

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.data.model.Offer
import com.hn_2452.shoes_nike.data.model.UserOffer
import com.hn_2452.shoes_nike.databinding.ItemPromoBinding
import com.hn_2452.shoes_nike.utility.toDayString
import com.hn_2452.shoes_nike.utility.toVND
import javax.inject.Inject


val UserOfferDiff = object : DiffUtil.ItemCallback<UserOffer>() {
    override fun areItemsTheSame(oldItem: UserOffer, newItem: UserOffer)
        = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UserOffer, newItem: UserOffer)
        = oldItem == newItem

}

class OfferAdapter @Inject constructor() : ListAdapter<UserOffer, OfferViewHolder>(UserOfferDiff) {

    var mOnClick: (UserOffer) -> Unit = {}
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
    private val mOnClick: (UserOffer) -> Unit,
    private val mOnDetail: (Offer) -> Unit,
    private val mApplyPrice: Long = 0
) : RecyclerView.ViewHolder(mBinding.root) {

    fun bind(userOffer: UserOffer) {
        if(userOffer.offer.valueToApply > mApplyPrice) {
            mBinding.root.setCardBackgroundColor(Color.parseColor("#DDDDDD"))
        } else {
            mBinding.root.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        }

        val sale = if (userOffer.offer.discountUnit == 0) {
            "Giảm giá ${userOffer.offer.discount}%"
        } else {
            "Giảm giá ${userOffer.offer.discount.toVND()}"
        }

        val saleMore = if (userOffer.offer.maxDiscount != -1L) {
            "Giảm tối đa ${userOffer.offer.maxDiscount.toVND()} cho đơn hàng từ ${userOffer.offer.valueToApply.toVND()}"
        } else {
            "Áp dụng cho đơn hàng từ ${userOffer.offer.valueToApply.toVND()}"
        }

        mBinding.tvSale.text = sale
        mBinding.tvSaleMore.text = saleMore
        mBinding.tvTitle.text = userOffer.offer.title
        mBinding.tvExpired.text = "Hết hạn ${userOffer.offer.endTime.toDayString()}"
        mBinding.tvDetail.setOnClickListener {
            mOnDetail(userOffer.offer)
        }
        mBinding.root.setOnClickListener {
            mOnClick(userOffer)
        }
    }


}
