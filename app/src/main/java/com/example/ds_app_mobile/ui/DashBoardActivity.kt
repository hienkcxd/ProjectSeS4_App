package com.example.ds_app_mobile.ui


import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.ds_app_mobile.R
import com.example.ds_app_mobile.databinding.ActivityDashBoardBinding
import kotlin.math.log

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var nightMode:Boolean = false
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        nightMode = sharedPreferences.getBoolean("night", false)
        editor = sharedPreferences.edit()
        if(nightMode){
            binding.btnDarkMode.isEnabled = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.btnDarkMode.setTextColor(ContextCompat.getColor(this, R.color.active_button))
            val newDrawable = ContextCompat.getDrawable(this, R.drawable.night_mode_active_50)
            binding.btnDarkMode.setCompoundDrawablesRelativeWithIntrinsicBounds(null, newDrawable, null, null)
        }else{
            binding.btnLightMode.isEnabled = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.btnLightMode.setTextColor(ContextCompat.getColor(this, R.color.active_button))
            val newDrawable = ContextCompat.getDrawable(this, R.drawable.light_mode_active_50)
            binding.btnLightMode.setCompoundDrawablesRelativeWithIntrinsicBounds(null, newDrawable, null, null)
        }

        binding.btnDarkMode.setOnClickListener {
            if (!nightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("night", true)
            }
            editor.apply()
        }

        binding.btnLightMode.setOnClickListener {
            if (nightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("night", false)
            }
            editor.apply()
        }
    }
}