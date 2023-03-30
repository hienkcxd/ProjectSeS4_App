package com.example.ds_app_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ds_app_mobile.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}