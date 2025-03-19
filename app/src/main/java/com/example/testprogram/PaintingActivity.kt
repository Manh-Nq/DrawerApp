package com.example.testprogram

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testprogram.databinding.ActivityPaintingBinding
import com.example.testprogram.ui.FileManager
import com.example.testprogram.ui.adapter.PreviewStickerAdapter
import com.example.testprogram.ui.adapter.StickerData
import com.example.testprogram.ui.assignViews
import com.example.testprogram.ui.custom.StickerDrawer
import com.example.testprogram.ui.custom.dpToPx
import com.example.testprogram.ui.custom.model.Sticker
import com.example.testprogram.ui.dialog.StickerDialog
import com.example.testprogram.ui.listener.CustomOnSeekBarChangeListener
import com.example.testprogram.ui.toSticker
import kotlinx.coroutines.launch
import java.util.Collections

class PaintingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPaintingBinding
    private val fileManager by lazy { FileManager() }
    private val previewAdapter by lazy { PreviewStickerAdapter(onItemClicked =this::handleRemoveSticker) }
    private var stickers: MutableList<Sticker> = ArrayList()


    val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0) { // Allow dragging in any direction

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.absoluteAdapterPosition
            val toPosition = target.absoluteAdapterPosition

            Collections.swap(stickers, fromPosition, toPosition)
            binding.drawingView.reorderStickers(stickers)

            recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }
    }

    // Attach ItemTouchHelper to RecyclerView
    val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityPaintingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() = with(binding) {
        assignViews(editImg, eraseImg, saveImg, stickerImg, backImg)
        changeSelectedMode(false)

        seekBar.setOnSeekBarChangeListener(object : CustomOnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                drawingView.setWidthPaint(progress)
            }
        })

        initPreviews()

    }

    private fun initPreviews() = with(binding.rvPreview) {
        layoutManager = LinearLayoutManager(this@PaintingActivity, LinearLayoutManager.HORIZONTAL, false)
        adapter = previewAdapter
        itemTouchHelper.attachToRecyclerView(this)

        setHasFixedSize(true)
    }

    private fun handleRemoveSticker(item: Sticker?) {
        val currentItem = item?: return

        stickers.removeIf { it.id == currentItem.id }
        previewAdapter.submitList(stickers.toMutableList())

        binding.drawingView.removeSticker(currentItem)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.editImg.id -> {
                changeSelectedMode(false)
                binding.drawingView.editPaint()
            }

            binding.eraseImg.id -> {
                changeSelectedMode(true)
                binding.drawingView.erasePaint()
            }

            binding.stickerImg.id -> showStickerDialog()

            binding.saveImg.id -> onSaveImage()

            binding.backImg.id -> onBackPressed()
        }
    }


    private fun changeSelectedMode(isErase: Boolean) {
        binding.editImg.isActivated = !isErase
        binding.eraseImg.isActivated = isErase
    }

    private fun onSaveImage() {
        requestStoragePermissions(onGranted = {
            lifecycleScope.launch {
                fileManager.saveDrawing(this@PaintingActivity, binding.drawingView)
                Toast.makeText(this@PaintingActivity, "Image saved successfully!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun showStickerDialog() {
        val dialog = StickerDialog(this, onItemClicked = {
            if (it == null) return@StickerDialog
            val bitmap = createBitmapFromRes(it) ?: return@StickerDialog

            val newSticker = bitmap.toSticker

            stickers.add(newSticker)

            Log.d("ManhNQ", "showStickerDialog: $stickers")

            previewAdapter.submitList(stickers.toMutableList())

            binding.drawingView.addSticker(newSticker)
        })

        dialog.show()
    }

    private fun createBitmapFromRes(it: StickerData, size: Float = 56f): Bitmap? {
        val bm = BitmapFactory.decodeResource(resources, it.resId)
        bm?.let { bitmap ->
            val size = dpToPx(size).toInt()

            return Bitmap.createScaledBitmap(bitmap, size, size, true)
        }
        return null
    }

    private fun requestStoragePermissions(onGranted: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                onGranted()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.parse("package:$packageName")
                intent.data = uri
                startActivityForResult(intent, 1001)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onGranted()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1001
                )
            }
        } else {
            onGranted()
        }
    }


}