package com.example.mangosoft

// Imports for handling intents, permissions, file management, and UI components
import android.content.Intent // Allows starting new activities or passing data between them
import android.content.pm.PackageManager // Used for managing app permissions
import android.net.Uri // Represents a Uniform Resource Identifier (URI) reference
import android.os.Build // Accesses information about the device's Android version
import android.os.Bundle // Required for saving and restoring activity state
import android.provider.MediaStore // Provides access to images and other media
import android.widget.LinearLayout // Layout view for organizing child views linearly
import android.widget.Toast // Provides simple popup messages to the user
import androidx.activity.result.contract.ActivityResultContracts // Manages results from activities (e.g., photo capture)
import androidx.appcompat.app.AppCompatActivity // Base class for activities with backward compatibility features
import androidx.core.app.ActivityCompat // Helper for managing permission requests
import androidx.core.content.ContextCompat // Accesses resources like permissions
import androidx.core.content.FileProvider // Allows secure file sharing between apps
import java.io.File // Represents a file in the filesystem

class HomeActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 100 // Code for handling camera permission request results
    private val GALLERY_REQUEST_CODE = 101 // Code for handling gallery permission request results
    private lateinit var currentImageUri: Uri // Stores URI of the current image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Sets the home screen layout

        // Finds and assigns the gallery and camera button containers
        val uploadButton: LinearLayout = findViewById(R.id.galleryButtonContainer)
        val cameraButton: LinearLayout = findViewById(R.id.cameraButtonContainer)

        // Set click listener for gallery button to check and request necessary permissions
        uploadButton.setOnClickListener {
            if (checkGalleryPermissions()) {
                openGallery() // Opens the gallery if permissions are granted
            }
        }

        // Set click listener for camera button to check and request necessary permissions
        cameraButton.setOnClickListener {
            if (checkCameraPermissions()) {
                openCamera() // Opens the camera if permissions are granted
            }
        }
    }

    // Launches an intent to open the gallery and pick an image
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent) // Starts gallery activity for a result
    }

    // Launches an intent to open the camera and capture an image
    private fun openCamera() {
        // Creates a temporary file to store the captured image
        val photoFile = createImageFile()
        // Provides a URI for the photo file for secure sharing with other apps
        currentImageUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.provider", photoFile)

        // Creates and launches the camera intent with the image URI as output location
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri)
        }
        cameraLauncher.launch(intent) // Starts camera activity for a result
    }

    // Handles the result from the gallery activity
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                currentImageUri = data?.data ?: return@registerForActivityResult // Gets the image URI
                // Opens MangoResultActivity to display the selected image
                val intent = Intent(this, MangoResultActivity::class.java)
                intent.putExtra("imageUri", currentImageUri) // Passes image URI to the next activity
                startActivity(intent)
            }
        }

    // Handles the result from the camera activity
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Opens MangoResultActivity to display the captured image
                val intent = Intent(this, MangoResultActivity::class.java)
                intent.putExtra("imageUri", currentImageUri) // Passes image URI to the next activity
                startActivity(intent)
            }
        }

    // Creates a temporary image file to store the captured image
    private fun createImageFile(): File {
        val imageFileName = "JPEG_${System.currentTimeMillis()}_" // Unique file name using timestamp
        val storageDir = getExternalFilesDir(null) // Directory for storing external files
        return File.createTempFile(imageFileName, ".jpg", storageDir) // Creates and returns the file
    }

    // Checks and requests gallery permissions for devices below Android Q
    private fun checkGalleryPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true // No need for storage permissions on Android 10 (Q) and above
        } else {
            // Checks and requests READ_EXTERNAL_STORAGE permission if not granted
            val storagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_REQUEST_CODE)
                false
            } else {
                true
            }
        }
    }

    // Checks and requests camera and storage permissions
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

    // Handles permission request results
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                // Opens camera if permission was granted
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            GALLERY_REQUEST_CODE -> {
                // Opens gallery if permission was granted
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
