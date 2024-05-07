package com.example.testprogram.ui.dialog

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

class StickerDialog(private val onItemClicked: (StickerData?) -> Unit) : DialogFragment() {
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
        list.add(StickerData(R.drawable.test))
        list.add(StickerData(R.drawable.test2))
        list.add(StickerData(R.drawable.test3))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = StickerDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        rvSticker.layoutManager = GridLayoutManager(this@StickerDialog.context, 3)
        rvSticker.adapter = adapter
        rvSticker.setHasFixedSize(true)
        adapter.submitList(list)
    }
}