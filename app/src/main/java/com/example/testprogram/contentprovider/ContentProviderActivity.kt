package com.example.testprogram.contentprovider


import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.testprogram.databinding.AnimateActivityBinding

class MainActivity : ComponentActivity() {

    private lateinit var insertBtn: Button
    private lateinit var getValuesBtn: Button
    private lateinit var resultTxt: TextView
    private val TAG = "ManhNQ"
    private val serverAuthorityEntry = "com.example.apilessonapp"
    private val tableName = "students"
    private val contentUri = "content://$serverAuthorityEntry/$tableName"
    private val PERMISSION_REQUEST_CODE: Int = 1

    private lateinit var binding: AnimateActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AnimateActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initViews()
        addListeners()
        checkCustomPermission()
    }

    private fun checkCustomPermission() {
        if (ContextCompat.checkSelfPermission(this, "com.example.apilessonapp.READ_DATABASE")
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf("com.example.apilessonapp.READ_DATABASE"),
                PERMISSION_REQUEST_CODE
            )
        }
        if (ContextCompat.checkSelfPermission(this, "com.example.apilessonapp.WRITE_DATABASE")
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf("com.example.apilessonapp.WRITE_DATABASE"),
                2
            )
        }
    }

    private fun checkValidPermission(onAccept: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val uri = Uri.parse("package:$packageName")
                startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        uri
                    )
                )
            } else {
                onAccept()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1001
            )
        }
    }

    private fun addListeners() {
        getValuesBtn.setOnClickListener {
            checkValidPermission {
                try {
                    val result = queryData(contentResolver)
                    if (result.isNotEmpty()) {
                        resultTxt.text = result.toString()
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "get Exception: ${e.printStackTrace()}")
                }
            }
        }
    }

    private fun insertData(contentResolver: ContentResolver) {
        val uri = Uri.parse(contentUri)
        val values = ContentValues().apply {
            put("name", "ManhNQ")
            put("age", 10)
        }
        val newUri = contentResolver.insert(uri, values)
        Log.d(TAG, "Inserted URI: $newUri")
    }

    private fun queryData(contentResolver: ContentResolver): List<Student> {
        val students = mutableListOf<Student>()
        val uri = Uri.parse(contentUri)
        val projection = arrayOf("name", "age")
        val cursor = contentResolver.query(uri, projection, null, null, null)
        Log.d(TAG, "queryData: $cursor")
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow("name"))
                val age = it.getString(it.getColumnIndexOrThrow("age"))
                students.add(Student(name, age))
            }
        }
        return students
    }

    private fun initViews() {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
