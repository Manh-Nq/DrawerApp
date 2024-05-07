package com.example.testprogram.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class StickerAdapter(private val onItemClicked: (StickerData?) -> Unit) :
    ListAdapter<StickerData, StickerViewHolder>(StickerDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        return StickerViewHolder.create(viewGroup = parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }
}


class StickerDiff : DiffUtil.ItemCallback<StickerData>() {
    override fun areItemsTheSame(oldItem: StickerData, newItem: StickerData): Boolean {
        return oldItem.resId == newItem.resId
    }

    override fun areContentsTheSame(oldItem: StickerData, newItem: StickerData): Boolean {
        return false
    }

}