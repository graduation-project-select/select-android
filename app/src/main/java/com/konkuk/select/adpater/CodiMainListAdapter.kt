package com.konkuk.select.adpater

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.konkuk.select.R
import com.konkuk.select.model.Codi
import com.konkuk.select.network.Fbase

class CodiMainListAdapter(val ctx:Context, val codiTagRefList:ArrayList<DocumentReference>):RecyclerView.Adapter<CodiMainListAdapter.ListHolder>() {

    var codiMainListItemAdapterArray: ArrayList<CodiMainListItemAdapter> = arrayListOf()
    var codiListArray: ArrayList<ArrayList<Codi>> = arrayListOf()

    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tv_title: TextView = itemView.findViewById(R.id.tv_title)
        var rv_codiList: RecyclerView = itemView.findViewById(R.id.rv_codiList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        initAdapter()
        val v:View = LayoutInflater.from(parent.context).inflate(R.layout.item_codi_main_list, parent, false)
        return ListHolder(v)
    }

    private fun initAdapter(){
        for((index, tagRef) in codiTagRefList.withIndex()){
            codiListArray.add(arrayListOf())
            codiMainListItemAdapterArray.add(CodiMainListItemAdapter(ctx, codiListArray[index]))
            getCodiListByTag(tagRef, index)
        }
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        codiTagRefList[position].get().addOnSuccessListener {
            holder.tv_title.text = it["name"] as String
            holder.rv_codiList.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
            holder.rv_codiList.adapter = codiMainListItemAdapterArray[position]
        }
    }

    override fun getItemCount(): Int {
        return codiTagRefList.size
    }

    private fun getCodiListByTag(tagRef: DocumentReference, index:Int) {
        tagRef.get().addOnSuccessListener {
            Fbase.CODI_REF
                .whereArrayContains("tags", tagRef) // TODO 최신순으로 정렬하기
                .whereEqualTo("uid", Fbase.uid)
                .get().addOnSuccessListener { documents ->
                    codiListArray[index].clear()
                    for (document in documents) {
                        val codiObj = Fbase.getCodi(document)
                        codiListArray[index].add(codiObj)
                    }
                    codiMainListItemAdapterArray[index].notifyDataSetChanged()
                }
        }
    }
}