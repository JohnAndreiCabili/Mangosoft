package com.example.mangosoft

// Imports necessary for UI, image processing, file handling, and displaying messages
import android.media.MediaScannerConnection // Scans and adds files to the gallery for user access
import android.graphics.Bitmap // Represents an image or bitmap to manipulate or save
import android.net.Uri // Handles Uniform Resource Identifier (URI) references
import android.os.Bundle // Provides a mapping from keys to values for state persistence
import android.widget.Button // Represents a clickable button view
import android.widget.ImageView // Represents a view for displaying images
import android.widget.TextView // Represents a view for displaying text
import androidx.appcompat.app.AppCompatActivity // Base class for activities with backward compatibility
import android.content.Intent // Allows starting new activities or passing data between them
import android.os.Environment // Provides access to standard storage directories
import android.view.View // Represents basic building blocks for creating interactive UI components
import android.widget.Toast // Displays short popup messages to the user
import java.io.File // Represents a file in the filesystem
import java.io.FileOutputStream // Provides a stream for writing data to files
import java.text.SimpleDateFormat // Formats dates and times
import java.util.Date // Represents date and time
import java.util.Locale // Specifies formatting rules for locale-specific data

class MangoResultActivity : AppCompatActivity() {
    // Declare UI components
    private lateinit var uploadedImage: ImageView // Displays uploaded image
    private lateinit var classificationText: TextView // Shows mango classification details
    private lateinit var accuracyText: TextView // Shows accuracy percentage
    private lateinit var classText: TextView // Shows mango quality class
    private lateinit var priceRangeText: TextView // Displays estimated price range
    private lateinit var dateText: TextView // Shows current date and time
    private lateinit var errorMessage: TextView // Displays error message if no image found
    private lateinit var scanAgainButton: Button // Button to scan another image
    private lateinit var saveCopyButton: Button // Button to save a screenshot copy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mango_result) // Sets up the layout for the result screen

        // Initialize UI components by finding them in the layout
        uploadedImage = findViewById(R.id.uploadedImage)
        classificationText = findViewById(R.id.classificationText)
        accuracyText = findViewById(R.id.accuracyText)
        classText = findViewById(R.id.classText)
        priceRangeText = findViewById(R.id.priceRangeText)
        dateText = findViewById(R.id.dateText) // For displaying date and time
        errorMessage = findViewById(R.id.errorMessage)
        scanAgainButton = findViewById(R.id.scanAgainButton)
        saveCopyButton = findViewById(R.id.saveCopyButton)

        // Set up a click listener to return to HomeActivity for rescanning
        scanAgainButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent) // Start HomeActivity for a new scan
            finish() // Close the result activity
        }

        // Set up a click listener to save a screenshot when the save button is pressed
        saveCopyButton.setOnClickListener {
            takeScreenshot() // Call function to save a screenshot
        }

        // Retrieve the image URI passed from HomeActivity
        val currentImageUri: Uri? = intent.getParcelableExtra("imageUri")

        // Retrieve the price value passed from the HomeActivity
        val finalPriceValue: String? = intent.getStringExtra("priceValue")

        // Retrieve the confidence value passed from the HomeActivity
        val finalConfidence: Float = intent.getFloatExtra("confidence", -1f)

        // Retrieve the mango_type value passed from the HomeActivity
        val finalType: String? = intent.getStringExtra("type")

        // Retrieve the mango_class value passed from the HomeActivity
        val finalClass: String? = intent.getStringExtra("mangoClass")

        if (currentImageUri != null) {
            // Display the image in the ImageView
            uploadedImage.setImageURI(currentImageUri)

            // Set classification
            classificationText.text = "Classification: " + finalType

            // Set confidence
            if (finalConfidence != 0F) {
                accuracyText.text = "Confidence: " + finalConfidence + "%"
            }
            else{
                accuracyText.text = ""
            }

            //Set class
            classText.text = finalClass

            //Set price
            if (finalPriceValue != "Unknown") {
                priceRangeText.text = "Estimated Price: Php " + finalPriceValue + "/kg"
            }
            else{
                priceRangeText.text = "Estimated Price: " + finalPriceValue
            }

            // Get and display the current date and time
            val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            dateText.text = "$currentDateTime" // Display formatted date and time

            errorMessage.visibility = TextView.GONE // Hide error message if image is displayed
        } else {
            uploadedImage.setImageDrawable(null) // Clear the image view if no image was passed
            errorMessage.visibility = TextView.VISIBLE // Show error message if no image
        }
    }

    // Function to take and save a screenshot of the current layout
    private fun takeScreenshot() {
        // Create a bitmap from the layout
        val bitmap = Bitmap.createBitmap(window.decorView.width, window.decorView.height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        window.decorView.draw(canvas) // Draws the layout onto the canvas

        // Specify the album name and directory for saving screenshots
        val albumName = "Mangosoft"
        val albumDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName)

        // Create the directory if it doesn’t already exist
        if (!albumDirectory.exists()) {
            albumDirectory.mkdirs()
        }

        // Save the bitmap as a PNG file in the gallery
        val file = File(albumDirectory, "MangoResult_${System.currentTimeMillis()}.png")
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // Compress and save image as PNG
                Toast.makeText(this, "Screenshot saved to gallery: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            }
            // Ensure file is scanned and available in the gallery immediately
            MediaScannerConnection.scanFile(this, arrayOf(file.toString()), null, null)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save screenshot", Toast.LENGTH_SHORT).show()
        }
    }
}
