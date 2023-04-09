package com.example.ds_app_mobile.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ds_app_mobile.databinding.ActivityUploadBinding


class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var nightMode: Boolean = false

    //upload video
    private val VIDEO_PICK_GALLERY_CODE = 100
    private val VIDEO_PICK_CAM_CODE = 101
    private val CAMERA_REQUEST_CODE = 102
    private lateinit var cameraPermission : Array<String>
    private var videoUri : Uri ?=null
    private lateinit var videoView: VideoView
    private var title:String =""
    //progress bar
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        nightMode = sharedPreferences.getBoolean("night", false)
        videoView = binding.viewVideo
        if(nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        //init camera permision
        cameraPermission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //init progressbar
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Uploading video...")
        progressDialog.setCanceledOnTouchOutside(false)
        //handle button upload video
        binding.btnUpload.setOnClickListener {
            title = binding.edtTitle.text.toString().trim()
            if(TextUtils.isEmpty(title)){
                Toast.makeText(this, "Title is required", Toast.LENGTH_LONG).show()
            }else if (videoUri == null){
                Toast.makeText(this, "pick the video first", Toast.LENGTH_LONG).show()
            }else{
                uploadVideoFirebase()
            }
        }
        //handle button chose video
        binding.btnChoseFile.setOnClickListener {
            videoPickDialog()
        }
    }

    private fun uploadVideoFirebase() {
        //show progress
        progressDialog.show()

        ///time stamp
        val timeStamp = System.currentTimeMillis()
    }

    private fun setVideoToView(){
        //set the video picked to view
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        //set mediacontroller
        videoView.setMediaController(mediaController)
        //set video Uri
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()
        videoView.setOnPreparedListener {
            //when video ready, by default don't play automatic
            videoView.pause()
        }
    }
    private fun videoPickDialog(){
        //option to display dialog
        val option = arrayOf("Camera", "Gallery")

        //alert dialog
        val builder = AlertDialog.Builder(this)
        //title
        builder.setTitle("Pick Video From").setItems(option){
            dialogInterfaces, i ->
            if(i == 0){
                if(checkCameraPermission()){
                    requestCameraPermission()
                }else{
                    pickVideoCamera()
                }
            }else{
                pickVideoGallery()
            }
        }.show()
    }

    private fun requestCameraPermission(){
        ActivityCompat.requestPermissions(
            this,
            cameraPermission,
            CAMERA_REQUEST_CODE
        )
    }

    private fun checkCameraPermission():Boolean{

        val result1 = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val result2 = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        return result1&&result2
    }

    private fun pickVideoGallery(){
        //video pick intent gallery
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Choose video"),
            VIDEO_PICK_GALLERY_CODE
        )
    }

    private fun pickVideoCamera(){
        //video pick intent gallery
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, VIDEO_PICK_CAM_CODE)
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.onTouchEvent(event)
    }

    //handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_REQUEST_CODE ->
                if (grantResults.size>0){
                    val cameraAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccept = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if(cameraAccept&&storageAccept){
                        //both permission allowed
                        pickVideoCamera()
                    }else{
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //handle video pick result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK){
            if(requestCode == VIDEO_PICK_CAM_CODE){
                //video picked from camera
                videoUri = data!!.data
                setVideoToView()
            }
            else if(requestCode == VIDEO_PICK_GALLERY_CODE){
                videoUri = data!!.data
                setVideoToView()
            }
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}