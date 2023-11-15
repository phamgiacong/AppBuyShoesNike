package com.hn_2452.shoes_nike.ui.orders.topup.topuppayment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.PayModel

class PayAdapter(private val list: List<PayModel>) :
    RecyclerView.Adapter<PayAdapter.PayViewHolder>() {

    inner class PayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val name: TextView = view.findViewById(R.id.tvname)
        val select: ImageView = view.findViewById(R.id.select)
        fun bindView(index: Int) {
            val item = list[index]
            item.icon?.let { icon.setImageResource(it) }
            name.text = item.name

            var count = 0
            select.setOnClickListener {
                if (count % 2 == 0) {
                    select.setImageResource(R.drawable.circle_selected)
                } else {
                    select.setImageResource(R.drawable.circle)
                }
                count++
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayViewHolder {
        return PayViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_pay, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PayViewHolder, position: Int) {
        holder.bindView(position)
    }
}