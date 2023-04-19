package com.example.ds_app_mobile.io.sharepreference

import android.content.Context
import android.content.SharedPreferences

class JwtSharePreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "jwt_prefs"
        private const val JWT_KEY = "jwt"
        private const val USER_NAME = "user"
        private const val DEVICE_ID = "deviceId"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveJwt(jwt: String) {
        preferences.edit().putString(JWT_KEY, jwt).apply()
    }
    fun saveDeviceId(deviceId: String) {
        preferences.edit().putString(DEVICE_ID, deviceId).apply()
    }
    fun getDeviceId(): String? {
        return preferences.getString(DEVICE_ID, null)
    }
    fun saveUser(user: String) {
        preferences.edit().putString(USER_NAME, user).apply()
    }
    fun getUser(): String? {
        return preferences.getString(USER_NAME, null)
    }
    fun getJwt(): String? {
        return preferences.getString(JWT_KEY, null)
    }

    fun clearJwt() {
        preferences.edit().remove(JWT_KEY).apply()
    }
    fun clearUser() {
        preferences.edit().remove(USER_NAME).apply()
    }
}