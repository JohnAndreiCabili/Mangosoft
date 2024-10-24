package com.example.mangosoft

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

class HomeActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 101
    private lateinit var currentImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val uploadButton: LinearLayout = findViewById(R.id.galleryButtonContainer)
        val cameraButton: LinearLayout = findViewById(R.id.cameraButtonContainer)

        uploadButton.setOnClickListener {
            if (checkGalleryPermissions()) {
                openGallery()
            }
        }

        cameraButton.setOnClickListener {
            if (checkCameraPermissions()) {
                openCamera()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun openCamera() {
        val photoFile = createImageFile()
        currentImageUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.provider", photoFile)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri)
        }
        cameraLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                currentImageUri = data?.data ?: return@registerForActivityResult
                val intent = Intent(this, MangoResultActivity::class.java)
                intent.putExtra("imageUri", currentImageUri)
                startActivity(intent)
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = Intent(this, MangoResultActivity::class.java)
                intent.putExtra("imageUri", currentImageUri)
                startActivity(intent)
            }
        }

    private fun createImageFile(): File {
        val imageFileName = "JPEG_${System.currentTimeMillis()}_"
        val storageDir = getExternalFilesDir(null)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun checkGalleryPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true
        } else {
            val storagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_REQUEST_CODE)
                false
            } else {
                true
            }
        }
    }

    private fun checkCameraPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val writePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return if (cameraPermission != PackageManager.PERMISSION_GRANTED || writePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_REQUEST_CODE)
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            GALLERY_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
