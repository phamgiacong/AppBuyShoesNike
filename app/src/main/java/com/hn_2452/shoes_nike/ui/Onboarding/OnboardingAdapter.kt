package com.hn_2452.shoes_nike.ui.Onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Onboarding

class OnboardingAdapter(private val onboardingList: List<Onboarding>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imgBackgroud: ImageView = view.findViewById(R.id.imgBackgroud)
        private val tvTitle: TextView = view.findViewById(R.id.tvTitle)

        fun bindView(index: Int) {
            val item = onboardingList[index]

            item.imgBackgroud?.let { imgBackgroud.setImageResource(it) }
            tvTitle.text = item.tvTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_onboarding, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return onboardingList.size
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bindView(position)
    }
}