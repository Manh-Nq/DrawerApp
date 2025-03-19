package com.example.testprogram.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testprogram.R
import com.example.testprogram.databinding.StickerDialogBinding
import com.example.testprogram.ui.adapter.StickerAdapter
import com.example.testprogram.ui.adapter.StickerData

class StickerDialog(private val context: Context,private val onItemClicked: (StickerData?) -> Unit) : Dialog(context) {
    private var _binding: StickerDialogBinding? = null
    private val binding: StickerDialogBinding get() = _binding!!
    private val adapter: StickerAdapter by lazy {
        StickerAdapter(onItemClicked = {
            onItemClicked(it)
            dismiss()
        })

    }

    private val stickers = mutableListOf<StickerData>()
    init {
        stickers.add(StickerData(R.drawable.ic_sticker_1))
        stickers.add(StickerData(R.drawable.ic_sticker_2))
        stickers.add(StickerData(R.drawable.ic_sticker_3))
        stickers.add(StickerData(R.drawable.ic_sticker_4))
        stickers.add(StickerData(R.drawable.ic_sticker_5))
        stickers.add(StickerData(R.drawable.ic_sticker_7))
        stickers.add(StickerData(R.drawable.ic_sticker_8))
        stickers.add(StickerData(R.drawable.ic_sticker_9))
        stickers.add(StickerData(R.drawable.ic_sticker_10))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = StickerDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }


    private fun initViews() = with(binding) {
        rvSticker.layoutManager = GridLayoutManager(this@StickerDialog.context, 3)
        rvSticker.adapter = adapter
        rvSticker.setHasFixedSize(true)
        adapter.submitList(stickers)
    }
}