package com.konkuk.select.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.konkuk.select.R
import com.konkuk.select.model.Codi

class CodiItemAdapter(val item: ArrayList<Codi>) : RecyclerView.Adapter<CodiItemAdapter.ItemHolder>(){

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.clothcodi_iv)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CodiItemAdapter.ItemHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.box_clothcodi_item, parent, false)
        return ItemHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: CodiItemAdapter.ItemHolder, position: Int) {

    }
}