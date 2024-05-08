package com.example.testprogram

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.testprogram.databinding.ActivityMainBinding
import com.example.testprogram.databinding.ActivityPaintingBinding
import com.example.testprogram.ui.FileManager
import com.example.testprogram.ui.assignViews
import com.example.testprogram.ui.dialog.StickerDialog
import com.example.testprogram.ui.listener.CustomOnSeekBarChangeListener
import kotlinx.coroutines.launch

class PaintingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPaintingBinding
    private val fileManager by lazy { FileManager() }

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
            binding.drawingView.addSticker(it.resId)
        })

        dialog.show()
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