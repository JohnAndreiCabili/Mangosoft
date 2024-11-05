package com.example.mangosoft

// Imports for handling intents, delays, and app compatibility
import android.content.Intent // Allows starting new activities or passing data between them
import android.os.Bundle // Required for saving and restoring activity state
import android.os.Handler // Enables scheduled tasks after a delay
import android.os.Looper // Used to associate Handler with the main UI thread
import androidx.appcompat.app.AppCompatActivity // Base class for activities with backward compatibility features

// Activity class for displaying a splash screen at app launch
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen) // Sets the splash screen layout

        // Delays the transition from splash screen to onboarding screen for 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            // Intent to navigate to the OnboardingActivity after splash delay
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)  // Starts the onboarding activity
            finish()  // Closes SplashScreenActivity to prevent returning to it on back press
        }, 2000) // Delay time in milliseconds (2 seconds)
    }
}
