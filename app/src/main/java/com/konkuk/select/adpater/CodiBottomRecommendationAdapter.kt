package com.konkuk.select.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.select.R
import com.konkuk.select.model.Category

class CodiBottomRecommendationAdapter(var rcmdList:ArrayList<String>):
    RecyclerView.Adapter<CodiBottomRecommendationAdapter.ItemHolder>() {

    var itemClickListener:OnItemClickListener?=null

    interface OnItemClickListener {
        fun OnClickItem(holder: ItemHolder, view: View, data: String, position: Int)
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_category_label: TextView = itemView.findViewById(R.id.tv_category_label)
        var reloadBtn: ImageView = itemView.findViewById(R.id.reloadBtn)
        init {
            reloadBtn.setOnClickListener {
                itemClickListener?.OnClickItem(this, it, rcmdList[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.codi_bottom_recommendation_item, parent, false)
        return ItemHolder(v)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.tv_category_label.text = rcmdList[position]
    }

    override fun getItemCount(): Int {
        return rcmdList.size
    }
}