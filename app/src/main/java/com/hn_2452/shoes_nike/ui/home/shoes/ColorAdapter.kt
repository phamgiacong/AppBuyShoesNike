package com.hn_2452.shoes_nike.ui.home.shoes

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.databinding.LayoutShoesColorItemBinding
import javax.inject.Inject


class ColorAdapterHolder(private val view: LayoutShoesColorItemBinding) :
    RecyclerView.ViewHolder(view.root) {
    fun bind(colorData: Pair<String, Boolean>, onClick: (String) -> Unit) {
        if (colorData.second) {
            view.imvChecker.visibility = View.VISIBLE
        } else {
            view.imvChecker.visibility = View.INVISIBLE
        }
        view.root.backgroundTintList = ColorStateList.valueOf(Color.parseColor(colorData.first))
        view.root.setOnClickListener {
            onClick(colorData.first)
        }
    }
}

class ColorAdapter @Inject constructor() : RecyclerView.Adapter<ColorAdapterHolder>() {

    private var mOnClick: (String) -> Unit = { }
    private var mData: List<Pair<String, Boolean>> = emptyList()

    fun setOnClick(onClick: (String) -> Unit) {
        mOnClick = onClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Pair<String, Boolean>>) {
        mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ColorAdapterHolder(
            LayoutShoesColorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ColorAdapterHolder, position: Int) {
        val sizeData = mData[position]
        holder.bind(sizeData, mOnClick)
    }
}