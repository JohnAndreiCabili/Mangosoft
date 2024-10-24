package com.example.mangosoft

import android.media.MediaScannerConnection
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Environment
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MangoResultActivity : AppCompatActivity() {
    // Declare UI components
    private lateinit var uploadedImage: ImageView
    private lateinit var classificationText: TextView
    private lateinit var accuracyText: TextView
    private lateinit var classText: TextView
    private lateinit var priceRangeText: TextView
    private lateinit var dateText: TextView  // New TextView for date and time
    private lateinit var errorMessage: TextView
    private lateinit var scanAgainButton: Button  // Button to scan again
    private lateinit var saveCopyButton: Button  // Button to save a copy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mango_result)

        // Initialize UI components
        uploadedImage = findViewById(R.id.uploadedImage)
        classificationText = findViewById(R.id.classificationText)
        accuracyText = findViewById(R.id.accuracyText)
        classText = findViewById(R.id.classText)
        priceRangeText = findViewById(R.id.priceRangeText)
        dateText = findViewById(R.id.dateText)  // Initialize date TextView
        errorMessage = findViewById(R.id.errorMessage)
        scanAgainButton = findViewById(R.id.scanAgainButton)  // Initialize scan again button
        saveCopyButton = findViewById(R.id.saveCopyButton)  // Initialize save copy button

        // Set up the click listener for the scanAgainButton to navigate back to HomeActivity
        scanAgainButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set up the click listener for the saveCopyButton to take a screenshot
        saveCopyButton.setOnClickListener {
            takeScreenshot()
        }

        // Retrieve the image URI passed from HomeActivity
        val currentImageUri: Uri? = intent.getParcelableExtra("imageUri")

        if (currentImageUri != null) {
            uploadedImage.setImageURI(currentImageUri)

            classificationText.text = "Classification: Indian"
            accuracyText.text = "Accuracy: 95%"
            classText.text = "Class: A"
            priceRangeText.text = "Price Range: Php 100/kg"

            // Get the current date and time
            val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            dateText.text = "$currentDateTime"  // Set the date and time text

            errorMessage.visibility = TextView.GONE
        } else {
            uploadedImage.setImageDrawable(null)
            errorMessage.visibility = TextView.VISIBLE
        }
    }

    private fun takeScreenshot() {
        // Create a bitmap of the current layout
        val bitmap = Bitmap.createBitmap(window.decorView.width, window.decorView.height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        window.decorView.draw(canvas)

        // Create the album directory if it doesn't exist
        val albumName = "Mangosoft"
        val albumDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName)

        if (!albumDirectory.exists()) {
            albumDirectory.mkdirs()  // Create the album directory
        }

        // Save the bitmap to the gallery in the new album
        val file = File(albumDirectory, "MangoResult_${System.currentTimeMillis()}.png")
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                Toast.makeText(this, "Screenshot saved to gallery: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            }
            // Scan the file so it shows up in the gallery
            MediaScannerConnection.scanFile(this, arrayOf(file.toString()), null, null)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save screenshot", Toast.LENGTH_SHORT).show()
        }
    }
}
