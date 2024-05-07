package com.example.testprogram.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testprogram.databinding.IconViewBinding

data class StickerData(val resId: Int)
class StickerViewHolder(val binding: IconViewBinding, val onItemClicked: (StickerData?) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    private var currentItem: StickerData? = null;

    init {
        binding.root.setOnClickListener {
            onItemClicked(currentItem)
        }
    }

    fun bindView(data: StickerData) {
        currentItem = data
        binding.stickerIcon.setImageResource(data.resId)
    }


    companion object{
        fun create(viewGroup: ViewGroup,  onItemClicked: (StickerData?) -> Unit): StickerViewHolder {
            val binding = IconViewBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )

            return StickerViewHolder(binding, onItemClicked)
        }
    }

}