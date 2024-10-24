package com.example.mangosoft

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Use a Handler to introduce a delay before transitioning from Splash Screen to OnboardingActivity
        Handler(Looper.getMainLooper()).postDelayed({
            // Create an intent to start the OnboardingActivity after the splash screen
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)  // Start the onboarding activity
            finish()  // Close the SplashScreenActivity so it's not in the back stack
        }, 2000) // 2 seconds delay before switching activities
    }
}
