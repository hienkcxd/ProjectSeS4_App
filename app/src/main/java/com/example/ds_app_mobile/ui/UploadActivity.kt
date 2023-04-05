package com.example.ds_app_mobile.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import com.example.ds_app_mobile.databinding.ActivityAccountBinding
import com.example.ds_app_mobile.databinding.ActivityUploadBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.security.spec.PSSParameterSpec
import javax.crypto.spec.PSource

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var nightMode:Boolean = false
    private lateinit var storageReference : StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        nightMode = sharedPreferences.getBoolean("night", false)
        if(nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        storageReference = FirebaseStorage.getInstance().getReference("videos")
        binding.btnChoseFile.setOnClickListener {

        }

        binding.btnUpload.setOnClickListener {

        }
    }

    private fun chosenFile(){
        val intent = Intent()
        intent.setType("Video/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.onTouchEvent(event)
    }
}