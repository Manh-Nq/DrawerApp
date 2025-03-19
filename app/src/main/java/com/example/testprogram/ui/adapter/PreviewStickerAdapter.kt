package com.example.testprogram.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.testprogram.ui.custom.model.Sticker

class PreviewStickerAdapter(private val onItemClicked: (Sticker?) -> Unit) :
    ListAdapter<Sticker, PreviewStickerViewHolder>(PreviewStickerDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewStickerViewHolder {
        return PreviewStickerViewHolder.create(viewGroup = parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: PreviewStickerViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }
}


class PreviewStickerDiff : DiffUtil.ItemCallback<Sticker>() {
    override fun areItemsTheSame(oldItem: Sticker, newItem: Sticker): Boolean {
        return oldItem.bitmap.sameAs(newItem.bitmap)
    }

    override fun areContentsTheSame(oldItem: Sticker, newItem: Sticker): Boolean {
        return false
    }

}