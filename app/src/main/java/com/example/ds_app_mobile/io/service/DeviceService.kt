package com.example.ds_app_mobile.io.service

import android.util.Log
import com.example.ds_app_mobile.io.AuthInterceptor
import com.example.ds_app_mobile.io.response.DeviceResponse
import com.example.ds_app_mobile.io.response.FileResponse
import com.example.ds_app_mobile.io.response.StoreResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DeviceService {


    //lay danh sach thiet bị
    @GET(value = "user/device")
    fun getDeviceList(
        @Header("Authorization") token: String,
    ): Call<List<DeviceResponse>>


    //lay ds store cua user
    @GET(value = "user/store")
    fun getStoreList(
        @Header("Authorization") token: String
    ): Call<List<StoreResponse>>


    //lay ds file cua user
    @GET(value = "user/file-storage")
    fun getFileList(
        @Header("Authorization") token: String,
    ): Call<List<FileResponse>>


    companion object {
//        private const val BASE_URL = "http://192.168.1.128:8080/api/"
        private const val BASE_URL = "http://192.168.1.60:8080/api/"
//        private const val BASE_URL ="http://172.16.1.170:8080/api/"


        fun create(authToken: String): DeviceService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            Log.i("jwt", "gọi vào okHTTP ")
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(AuthInterceptor(authToken))
                .build()
            Log.i("jwt", "gọi vào retrofit ")
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(DeviceService::class.java)
        }
    }
}