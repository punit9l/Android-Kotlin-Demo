package com.t9l.androidkotlindemo.recyclerView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.t9l.androidkotlindemo.R
import kotlinx.android.synthetic.main.custom_item.view.*

class CustomAdapter(
        private val mutableList: MutableList<CustomItem>
): RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): CustomViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.custom_item, viewGroup, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItem(mutableList[position])
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: CustomItem) {
            itemView.textViewItem.text = item.name
        }

    }
}