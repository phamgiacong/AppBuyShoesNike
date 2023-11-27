package com.hn_2452.shoes_nike.ui.searching

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.data.model.ShoesType
import com.hn_2452.shoes_nike.databinding.LayoutShoesTypeItemBinding
import javax.inject.Inject

class ShoesTypeViewHolder(
    private val view: LayoutShoesTypeItemBinding,
    private val mOnClick: (ShoesType) -> Unit
) :
    RecyclerView.ViewHolder(view.root) {
    fun bind(shoesTypeData: Pair<ShoesType, Boolean>) {
        if (shoesTypeData.second) {
            view.tvShoesTypeName.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
            view.tvShoesTypeName.setTextColor(Color.WHITE)
        } else {
            view.tvShoesTypeName.backgroundTintList = null
            view.tvShoesTypeName.setTextColor(Color.BLACK)
        }
        view.tvShoesTypeName.text = shoesTypeData.first.name
        view.root.setOnClickListener {
            mOnClick(shoesTypeData.first)
        }
    }
}

class ShoesTypeAdapter @Inject constructor() : RecyclerView.Adapter<ShoesTypeViewHolder>() {

    private var mData: List<Pair<ShoesType, Boolean>> = emptyList()
    private var mOnClick: (ShoesType) -> Unit = {}

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Pair<ShoesType, Boolean>>) {
        mData = data
        notifyDataSetChanged()
    }

    fun setOnClick(onClick: (ShoesType) -> Unit) {
        mOnClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ShoesTypeViewHolder(
            LayoutShoesTypeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            mOnClick
        )

    override fun getItemCount() = mData.size
    override fun onBindViewHolder(holder: ShoesTypeViewHolder, position: Int) {
        holder.bind(mData[position])
    }
}


