package com.example.testprogram.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
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

    private val list = mutableListOf<StickerData>()
    init {
        list.add(StickerData(R.drawable.sticker_1))
        list.add(StickerData(R.drawable.sticker_2))
        list.add(StickerData(R.drawable.sticker_3))
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
        adapter.submitList(list)
    }
}