package com.hn_2452.shoes_nike.ui.review_shoes

import android.util.Log
import coil.load
import com.airbnb.epoxy.TypedEpoxyController
import com.hn_2452.shoes_nike.AN_MINUTE
import com.hn_2452.shoes_nike.A_DAY
import com.hn_2452.shoes_nike.A_HOUR
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.ShoesReview
import com.hn_2452.shoes_nike.databinding.LayoutShoesReviewItemBinding
import com.hn_2452.shoes_nike.utility.ViewBindingModel

class ShoesReviewController : TypedEpoxyController<List<ShoesReview>>() {

    class ShoesReviewModel(private val shoesReview: ShoesReview) :
        ViewBindingModel<LayoutShoesReviewItemBinding>(R.layout.layout_shoes_review_item) {
        override fun LayoutShoesReviewItemBinding.bind() {
            tvComment.text = shoesReview.comment
            tvPostingTime.text = getTime(shoesReview.postingTime)
            tvStar.text = "${shoesReview.rate} sao"
            tvUserName.text = shoesReview.user.name
            imvUser.load(shoesReview.user.avatar) {
                error(R.drawable.user_placeholder)
                placeholder(R.drawable.user_placeholder)
            }
        }

        private fun getTime(postingTime: Long): String {
            Log.i("Nike:", "getTime: $postingTime")
            val time = System.currentTimeMillis() - postingTime

            if (time > A_DAY) {
                return "${(time / A_DAY).toInt()} ngày trước"
            }

            if (time > A_HOUR) {
                return "${(time / A_HOUR).toInt()} giờ trước"
            }

            if (time > AN_MINUTE) {
                return "${(time / AN_MINUTE)} phút trước"
            }
            return "Vừa mới"
        }

    }

    override fun buildModels(data: List<ShoesReview>) {
        data.forEach {
            ShoesReviewModel(it).id(it.id).addTo(this)
        }
    }


}