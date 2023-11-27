package com.hn_2452.shoes_nike.ui.searching

import com.airbnb.epoxy.TypedEpoxyController
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Searching
import com.hn_2452.shoes_nike.databinding.LayoutRecentSearchItemBinding
import com.hn_2452.shoes_nike.utility.ViewBindingModel
import javax.inject.Inject

class RecentSearchAdapter @Inject constructor() : TypedEpoxyController<List<Searching>>() {

    private var mOnClick: (Searching) -> Unit = {}
    private var mOnDelete: (Searching) -> Unit = {}
    fun setOnClick(onClick: (Searching) -> Unit) {
        mOnClick = onClick
    }

    fun setOnDelete(onDelete: (Searching) -> Unit) {
        mOnDelete = onDelete
    }

    override fun buildModels(data: List<Searching>?) {
        data?.forEach {
            RecentSearchModel(it, mOnClick, mOnDelete).id(it.content).addTo(this)
        }
    }

    class RecentSearchModel(
        private val mSearching: Searching,
        private val mOnClick: (Searching) -> Unit,
        private val mOnDelete: (Searching) -> Unit
    ) : ViewBindingModel<LayoutRecentSearchItemBinding>(R.layout.layout_recent_search_item) {
        override fun LayoutRecentSearchItemBinding.bind() {
            tvContent.text = mSearching.content
            imvClear.setOnClickListener {
                mOnDelete(mSearching)
            }
            root.setOnClickListener {
                mOnClick(mSearching)
            }
        }

    }
}