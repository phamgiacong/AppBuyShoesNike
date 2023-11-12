package com.hn_2452.shoes_nike.ui.orders.e_wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.MyEWalletModel

class MyEWalletAdapter(private val list: List<MyEWalletModel>) :
    RecyclerView.Adapter<MyEWalletAdapter.MyEWalletViewHolder>() {
    inner class MyEWalletViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val backGround: ImageView = view.findViewById(R.id.background)
        val image: ImageView = view.findViewById(R.id.img_shoe)
        val name: TextView = view.findViewById(R.id.tv_name)
        val date: TextView = view.findViewById(R.id.tv_date)
        val time: TextView = view.findViewById(R.id.tv_time)
        val properties: TextView = view.findViewById(R.id.tv_properties)
        val arrow: ImageView = view.findViewById(R.id.arrow_down)

        fun bindView(index: Int) {
            val item = list[index]
            backGround.setImageResource(R.drawable.bg_circle)
            image.setImageResource(R.drawable.shoe_svgrepo_com)
            name.text = item.name
            date.text = item.date
            time.text = item.time
            properties.text = item.properties
            arrow.setImageResource(R.drawable.arrow_up_upload_svgrepo_com)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyEWalletAdapter.MyEWalletViewHolder {
        return MyEWalletViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_ewallet, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyEWalletAdapter.MyEWalletViewHolder, position: Int) {
       holder.bindView(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}