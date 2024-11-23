package com.example.mangosoft

// Imports for handling intents, permissions, file management, and UI components
import android.content.Intent // Allows starting new activities or passing data between them
import android.content.pm.PackageManager // Used for managing app permissions
import android.net.Uri // Represents a Uniform Resource Identifier (URI) reference
import android.os.Build // Accesses information about the device's Android version
import android.os.Bundle // Required for saving and restoring activity state
import android.provider.MediaStore // Provides access to images and other media
import android.util.Log
import android.widget.LinearLayout // Layout view for organizing child views linearly
import android.widget.Toast // Provides simple popup messages to the user
import androidx.activity.result.contract.ActivityResultContracts // Manages results from activities (e.g., photo capture)
import androidx.appcompat.app.AppCompatActivity // Base class for activities with backward compatibility features
import androidx.core.app.ActivityCompat // Helper for managing permission requests
import androidx.core.content.ContextCompat // Accesses resources like permissions
import androidx.core.content.FileProvider // Allows secure file sharing between apps
import com.example.mangosoft.model.RFRInput
import com.example.mangosoft.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File // Represents a file in the filesystem
import java.time.Year

import androidx.lifecycle.lifecycleScope
import java.io.IOException
import java.util.Calendar
import kotlin.properties.Delegates


class HomeActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 100 // Code for handling camera permission request results
    private val GALLERY_REQUEST_CODE = 101 // Code for handling gallery permission request results
    private lateinit var currentImageUri: Uri // Stores URI of the current image
    private lateinit var currentPriceValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Sets the home screen layout

        currentPriceValue = "test"

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
        galleryLauncher.launch(intent)
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

                //Model Initialization
                callCNNApi { modelInit() }

                // Opens MangoResultActivity to display the selected image
                //openMangoResultActivity()
            }
        }

    // Handles the result from the camera activity
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {

                //Model Initialization
                modelInit()

                // Opens MangoResultActivity to display the captured image
                //openMangoResultActivity()
            }
        }

    var resultConfidence: Float? = 0F
    var resultType: String? = null

    private fun callCNNApi(onResultReady: () -> Unit) {

        lifecycleScope.launch {
            try {
                // Convert URI to MultipartBody.Part
                val imageFile = uriToFile(currentImageUri)
                val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("file", imageFile.name, requestBody)

                // Call the CNN API
                val response = Repository().processCNN(imagePart)

                if (response.isSuccessful) {
                    response.body()?.let { yoloResponse ->

                        resultConfidence = (yoloResponse.confidence * 100).toFloat() // Convert to percentage
                        resultType = yoloResponse.mango_type

                        Log.d("Variables", "Confidence: $resultConfidence")
                        Log.d("Variables", "Mango_Type: $resultType")


                        // Log each result for debugging
                        Log.d("CNN_API", "Blemishes: ${yoloResponse.blemishes}")
                        Log.d("CNN_API", "Confidence: ${yoloResponse.confidence}")
                        Log.d("CNN_API", "Mango Type: ${yoloResponse.mango_type}")
                        Log.d("CNN_API", "Texture: ${yoloResponse.texture}")


                        // Notify that results are ready
                        onResultReady()
                    }
                } else {
                    Log.e("CNN_API", "Failed to fetch results: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("CNN_API", "Error calling CNN API", e)
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val contentResolver = applicationContext.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: throw IOException("Unable to open URI")
        val tempFile = File.createTempFile("image", ".jpg", cacheDir)
        tempFile.outputStream().use { outputStream -> inputStream.copyTo(outputStream) }
        return tempFile
    }

    //MODEL TRIAL
    private fun modelInit(): Unit {
        // Call CNN API with the selected image
        //callCNNApi()

        Log.d("TRY TRY TRY", "Confidence: $resultConfidence")
        Log.d("TRY TRY TRY", "Mango_Type: $resultType")

        // Determine the type
        val typeCarabao = if (resultType?.contains("K", ignoreCase = true) == true) 1 else 0
        val typeIndian = if (resultType?.contains("I", ignoreCase = true) == true) 1 else 0
        val typePico = if (resultType?.contains("P", ignoreCase = true) == true) 1 else 0

        // Determine the class
        val classClassA = if (resultType?.contains("CLASS A", ignoreCase = true) == true ||
            resultType?.contains("CLASS-A", ignoreCase = true) == true) 1 else 0
        val classClassB = if (resultType?.contains("CLASS B", ignoreCase = true) == true ||
            resultType?.contains("CLASS-B", ignoreCase = true) == true) 1 else 0
        val classClassC = if (resultType?.contains("CLASS C", ignoreCase = true) == true ||
            resultType?.contains("CLASS-C", ignoreCase = true) == true) 1 else 0
        val classClassD = if (resultType?.contains("CLASS D", ignoreCase = true) == true ||
            resultType?.contains("CLASS-D", ignoreCase = true) == true) 1 else 0
        val classClassE = if (resultType?.contains("CLASS E", ignoreCase = true) == true ||
            resultType?.contains("CLASS-E", ignoreCase = true) == true) 1 else 0



        // Get current year and month
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // 1-based month

        // Determine the month fields
        val monthFields = IntArray(12) { 0 } // Initialize all months to 0
        if (currentMonth in 1..12) {
            monthFields[currentMonth - 1] = 1 // Set the current month to 1
        }

        //RFR TRIAL
        val repo = Repository()

        val cnndata = RFRInput(
            Year = currentYear,
            Type_Carabao = typeCarabao,
            Type_Indian = typeIndian,
            Type_Pico = typePico,
            Class_ClassA = classClassA,
            Class_ClassB = classClassB,
            Class_ClassC = classClassC,
            Class_ClassD = classClassD,
            Class_ClassE = classClassE,
            Month_January = monthFields[0],
            Month_February = monthFields[1],
            Month_March = monthFields[2],
            Month_April = monthFields[3],
            Month_May = monthFields[4],
            Month_June = monthFields[5],
            Month_July = monthFields[6],
            Month_August = monthFields[7],
            Month_September = monthFields[8],
            Month_October = monthFields[9],
            Month_November = monthFields[10],
            Month_December = monthFields[11]
        )

        // Use CoroutineScope to call the suspend function
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = repo.processRFR(cnndata) // Calls the suspend function

                // Switch to Main thread to show Toast
                withContext(Dispatchers.Main) {
                    // Log the response body
                    Log.d("RFRResponse", "Response Body: ${res.body().toString()}")

                    currentPriceValue = res.body()!!.joinToString (", ").trim()
                    //Toast.makeText(this@HomeActivity, currentPriceValue, Toast.LENGTH_SHORT).show()

                    openMangoResultActivity()
                }
            } catch (e: Exception) {
                // Handle the error
                withContext(Dispatchers.Main) {

                    currentPriceValue = "Error"
                    //Toast.makeText(this@HomeActivity, currentPriceValue, Toast.LENGTH_LONG).show()

                    openMangoResultActivity()
                }
            }
        }
    }

    private fun openMangoResultActivity() {
        if (resultConfidence != null && resultType != null) {
            val currentType = when {
                resultType?.contains("K", ignoreCase = true) == true -> "Carabao"
                resultType?.contains("I", ignoreCase = true) == true -> "Indian"
                resultType?.contains("P", ignoreCase = true) == true -> "Pico"
                else -> "Unknown Type"
            }

            val currentMangoClass = when {
                resultType?.contains("CLASS A", ignoreCase = true) == true ||
                        resultType?.contains("CLASS-A", ignoreCase = true) == true -> "CLASS A"
                resultType?.contains("CLASS B", ignoreCase = true) == true ||
                        resultType?.contains("CLASS-B", ignoreCase = true) == true -> "CLASS B"
                resultType?.contains("CLASS C", ignoreCase = true) == true ||
                        resultType?.contains("CLASS-C", ignoreCase = true) == true -> "CLASS C"
                resultType?.contains("CLASS D", ignoreCase = true) == true ||
                        resultType?.contains("CLASS-D", ignoreCase = true) == true -> "CLASS D"
                resultType?.contains("CLASS E", ignoreCase = true) == true ||
                        resultType?.contains("CLASS-E", ignoreCase = true) == true -> "CLASS E"
                else -> "Unknown Class"
            }

            val intent = Intent(this, MangoResultActivity::class.java)
            intent.putExtra("imageUri", currentImageUri) // Passes image URI to the next activity
            intent.putExtra("priceValue", currentPriceValue) // Passes price value to the next activity
            intent.putExtra("confidence", resultConfidence) // Passes confidence value to the next activity
            intent.putExtra("type", currentType) // Passes mango_type value to the next activity
            intent.putExtra("mangoClass", currentMangoClass) // Passes mango_type value to the next activity
            startActivity(intent)
        } else {
//            Toast.makeText(this, "Data not ready yet. Please try again.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MangoResultActivity::class.java)
            intent.putExtra("imageUri", currentImageUri) // Passes image URI to the next activity
            intent.putExtra("priceValue", "Unknown") // Passes price value to the next activity
            intent.putExtra("confidence", 0F) // Passes confidence value to the next activity
            intent.putExtra("type", "Unknown") // Passes mango_type value to the next activity
            intent.putExtra("mangoClass", "Unknown") // Passes mango_type value to the next activity
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