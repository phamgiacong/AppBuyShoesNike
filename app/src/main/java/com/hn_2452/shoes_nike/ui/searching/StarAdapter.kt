package com.hn_2452.shoes_nike.ui.searching

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.databinding.LayoutStartItemBinding
import javax.inject.Inject

class StarAdapter @Inject constructor() : RecyclerView.Adapter<StarViewHolder>() {

    private var mOnClick: (Int) -> Unit = {}
    private var mData: List<Pair<Int, Boolean>> = emptyList()

    fun setOnClick(onClick: (Int) -> Unit) {
        mOnClick = onClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Pair<Int, Boolean>>) {
        mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        StarViewHolder(
            LayoutStartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mOnClick
        )

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: StarViewHolder, position: Int) =
        holder.bind(mData[position])

}

class StarViewHolder(
    private val view: LayoutStartItemBinding,
    private val onClick: (Int) -> Unit
) : RecyclerView.ViewHolder(view.root) {

    @SuppressLint("UseCompatTextViewDrawableApis")
    fun bind(starData: Pair<Int, Boolean>) {
        if (starData.second) {
            view.start.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
            view.start.setTextColor(Color.WHITE)
            view.start.compoundDrawableTintList = ColorStateList.valueOf(Color.WHITE)
        } else {
            view.start.backgroundTintList = null
            view.start.setTextColor(Color.BLACK)
            view.start.compoundDrawableTintList = ColorStateList.valueOf(Color.BLACK)
        }

        if(starData.first == -1) {
            view.start.text = "Tất cả"
        } else {
            view.start.text = starData.first.toString()
        }
        view.root.setOnClickListener {
            onClick(starData.first)
        }
    }
}
