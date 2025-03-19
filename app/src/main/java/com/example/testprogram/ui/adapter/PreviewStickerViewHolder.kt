package com.example.testprogram.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testprogram.databinding.IconViewBinding
import com.example.testprogram.databinding.PreviewItemViewBinding
import com.example.testprogram.ui.custom.model.Sticker

class PreviewStickerViewHolder(val binding: PreviewItemViewBinding, val onItemClicked: (Sticker?) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    private var currentItem: Sticker? = null;

    init {
        binding.root.setOnClickListener {
            onItemClicked(currentItem)
        }
    }

    fun bindView(data: Sticker) {
        currentItem = data
        binding.stickerIcon.setImageBitmap(data.bitmap)
    }


    companion object{
        fun create(viewGroup: ViewGroup,  onItemClicked: (Sticker?) -> Unit): PreviewStickerViewHolder {
            val binding = PreviewItemViewBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )

            return PreviewStickerViewHolder(binding, onItemClicked)
        }
    }

}