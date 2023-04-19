package com.example.ds_app_mobile.ui


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.ds_app_mobile.MainActivity
import com.example.ds_app_mobile.R
import com.example.ds_app_mobile.databinding.ActivityDashBoardBinding
import com.example.ds_app_mobile.io.response.DeviceResponse
import com.example.ds_app_mobile.io.response.FileResponse
import com.example.ds_app_mobile.io.response.StoreResponse
import com.example.ds_app_mobile.io.service.DeviceService
import com.example.ds_app_mobile.io.service.LoginService
import com.example.ds_app_mobile.io.sharepreference.JwtSharePreference
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesLogin: JwtSharePreference
    private var deviceList : List<String> = listOf()
    private var fileList : List<String> = listOf()
    private var storeList : List<String> = listOf()
    private var nightMode:Boolean = false
    private lateinit var editor: SharedPreferences.Editor
    private val deviceService: DeviceService by lazy {
        DeviceService.create(getJwt().toString())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        sharedPreferencesLogin = JwtSharePreference(this)
        binding.txtName.text = sharedPreferencesLogin.getUser()
        nightMode = sharedPreferences.getBoolean("night", false)
        editor = sharedPreferences.edit()
        lifecycleScope.launch {
            quantityDeviceOfUser()
            quantityVideoOfUser()
            quantityStoreOfUser()
            binding.txtDevice.text = deviceList.size.toString()
            binding.txtFile.text = fileList.size.toString()
            binding.txtStore.text = storeList.size.toString()
        }
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

        binding.btnLogout.setOnClickListener {
            clearLogin()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnUpload.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        binding.btnAccount.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }
    }
    private fun clearLogin() {
        sharedPreferencesLogin.clearJwt()
        sharedPreferencesLogin.clearUser()
    }
    private fun getJwt():String? {
       return sharedPreferencesLogin.getJwt()
    }

    private suspend fun quantityVideoOfUser(){
        try {
            val callFile = deviceService.getFileList(getJwt().toString())
            val response = callFile.awaitResponse()
            if (response.isSuccessful) {
                val fileResponse: List<FileResponse>? = response.body()
                Log.i("jwt", "jwt lay duoc ${fileResponse.toString()} ")
                if (fileResponse == null) {
                    Toast.makeText(applicationContext, "không có dữ liệu!!!", Toast.LENGTH_LONG).show()
                    Log.i("jwt", "file trống")
                    return
                } else {
                    fileList = fileResponse.map { it.fileName }
                    Log.i("jwt", "danh sach file: ${fileList.toString()}")
                }
            } else {
                Log.i("jwt", "khong lay duoc du lieu file")
                Toast.makeText(applicationContext, "error 403!!!", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("jwt", e.message ?: "Unknown error")
        }
    }
    private suspend fun quantityStoreOfUser(){
        try {
            val callStore = deviceService.getStoreList(getJwt().toString())
            val response = callStore.awaitResponse()
            if (response.isSuccessful) {
                val storeResponse: List<StoreResponse>? = response.body()
                Log.i("jwt", "jwt lay duoc ${storeResponse.toString()} ")
                if (storeResponse == null) {
                    Toast.makeText(applicationContext, "không có dữ liệu!!!", Toast.LENGTH_LONG).show()
                    Log.i("jwt", "store trống")
                    return
                } else {
                    storeList = storeResponse.map { it.storeName }
                    Log.i("jwt", "danh sach store: ${storeList.toString()}")
                }
            } else {
                Log.i("jwt", "khong lay duoc du lieu")
                Toast.makeText(applicationContext, "error 403!!!", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("jwt", e.message ?: "Unknown error")
        }
    }
    private suspend fun quantityDeviceOfUser(){
        try {
            val callDevice = deviceService.getDeviceList(getJwt().toString())
            val response = callDevice.awaitResponse()
            if (response.isSuccessful) {
                val deviceResponse: List<DeviceResponse>? = response.body()
                Log.i("jwt", "jwt lay duoc ${deviceResponse.toString()} ")
                if (deviceResponse == null) {
                    Toast.makeText(applicationContext, "không có dữ liệu!!!", Toast.LENGTH_LONG).show()
                    Log.i("jwt", "device trống")
                    return
                } else {
                    deviceList = deviceResponse.map { it.deviceName }
                    Log.i("jwt", "danh sach device: ${deviceList.toString()}")
                }
            } else {
                Log.i("jwt", "khong lay duoc du lieu")
                Toast.makeText(applicationContext, "error 403!!!", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e("jwt", e.message ?: "Unknown error")
        }
    }
}