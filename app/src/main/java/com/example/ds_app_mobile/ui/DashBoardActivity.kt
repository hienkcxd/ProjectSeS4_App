package com.example.ds_app_mobile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ds_app_mobile.R
import com.example.ds_app_mobile.databinding.ActivityDashBoardBinding

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}