package com.example.ds_app_mobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.ds_app_mobile.databinding.ActivityMainBinding
import com.example.ds_app_mobile.io.response.LoginResponse
import com.example.ds_app_mobile.io.service.LoginService
import com.example.ds_app_mobile.io.sharepreference.JwtSharePreference
import com.example.ds_app_mobile.ui.DashBoardActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesLogin: JwtSharePreference
    private var nightMode:Boolean = false
    private val loginService: LoginService by lazy {
        LoginService.create()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        sharedPreferencesLogin = JwtSharePreference(this)
        nightMode = sharedPreferences.getBoolean("night", false)
        if(nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        if(isLoggedIn()){
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSubmit.setOnClickListener{
            formLogin()
        }

    }

    private fun formLogin() {
        val usernameLogin = findViewById<EditText>(R.id.edtUsername).text.toString()
        val passwordLogin = findViewById<EditText>(R.id.edtPassword).text.toString()
        Log.i("jwt", usernameLogin)
        Log.i("jwt", passwordLogin)
        val call = loginService.login(usernameLogin, passwordLogin)
        call.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val loginResponse = response.body()
                    if(loginResponse == null){
                        Toast.makeText(applicationContext, "Login Error!!!", Toast.LENGTH_LONG).show()
                        return
                    }
                    else{
                        saveJwt(loginResponse.access_token)
                        saveUser(usernameLogin)
                        Log.i("jwt", "jwt hien tai: ${getJwt().toString()}")
                        gotoNext()
                    }
                }else{
                    Toast.makeText(applicationContext, "invalid username or password!!!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("jwt", t.message.toString())
                Toast.makeText(applicationContext, "login failed!!!", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.onTouchEvent(event)
    }
    private fun gotoNext() {
        val intent = Intent(this, DashBoardActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("jwt_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.contains("jwt") //kiểm tra xem đã lưu jwt_token trong SharedPreferences chưa
    }
    private fun saveJwt(jwt: String) {
        sharedPreferencesLogin.saveJwt(jwt)
    }

    private fun getJwt(): String? {
        return sharedPreferencesLogin.getJwt()
    }
    private fun saveUser(user: String) {
        sharedPreferencesLogin.saveUser(user)
    }



}