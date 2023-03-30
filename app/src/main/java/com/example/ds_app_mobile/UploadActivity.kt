package com.example.ds_app_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ds_app_mobile.databinding.ActivityAccountBinding
import com.example.ds_app_mobile.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}