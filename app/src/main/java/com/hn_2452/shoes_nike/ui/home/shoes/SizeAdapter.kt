package com.hn_2452.shoes_nike.ui.home.shoes

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.LayoutShoesSizeItemBinding
import javax.inject.Inject

class SizeAdapterHolder(
    private val view: LayoutShoesSizeItemBinding,
    private val onClick: (Int) -> Unit
) :
    RecyclerView.ViewHolder(view.root) {
    fun bind(sizeData: Pair<Int, Boolean>) {
        if (sizeData.second) {
            view.root.setBackgroundResource(R.drawable.bg_black_circle)
            view.tvSize.setTextColor(Color.WHITE)
        } else {
            view.root.setBackgroundResource(R.drawable.bg_circle_border)
            view.tvSize.setTextColor(Color.parseColor("#878787"))
        }
        view.tvSize.text = sizeData.first.toString()

        view.root.setOnClickListener {
            onClick(sizeData.first)
        }
    }
}

class SizeAdapter @Inject constructor() : RecyclerView.Adapter<SizeAdapterHolder>() {

    private lateinit var mOnClick: (Int) -> Unit
    private var mData: List<Pair<Int, Boolean>> = emptyList()
    fun setOnClickListener(onClick: (Int) -> Unit) {
        mOnClick = onClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Pair<Int, Boolean>>) {
        mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SizeAdapterHolder(
            LayoutShoesSizeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            mOnClick
        )

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: SizeAdapterHolder, position: Int) {
        val sizeData = mData[position]
        holder.bind(sizeData)
    }

}

