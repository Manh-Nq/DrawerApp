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
import com.example.testprogram.ui.FileManager
import com.example.testprogram.ui.assignViews
import com.example.testprogram.ui.dialog.StickerDialog
import com.example.testprogram.ui.listener.CustomOnSeekBarChangeListener
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() = with(binding) {
        assignViews(animationBtn, paintingBtn)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.animationBtn.id -> {

            }

            binding.paintingBtn.id -> {
                val intent = Intent(this, PaintingActivity::class.java)
                startActivity(intent)
            }
        }
    }

}