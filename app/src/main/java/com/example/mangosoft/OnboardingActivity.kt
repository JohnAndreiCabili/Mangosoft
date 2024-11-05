package com.example.mangosoft

// Imports necessary for UI components, animations, and navigation between activities
import android.animation.ValueAnimator // Handles animations with values over a time interval
import android.content.Intent // Allows navigation between activities
import android.os.Bundle // Bundle class to hold activity data state
import android.view.View // Provides functionality for interacting with views
import androidx.appcompat.app.AppCompatActivity // Base class for compatibility support
import androidx.viewpager2.widget.ViewPager2 // ViewPager for swiping through onboarding screens
import com.example.mangosoft.databinding.ActivityOnboardingBinding // Binding class for activity layout
import com.google.android.material.progressindicator.LinearProgressIndicator // Progress indicator bar for user navigation

class OnboardingActivity : AppCompatActivity() {

    // Declare ViewPager, adapter, and view binding variables
    private lateinit var viewPager: ViewPager2 // ViewPager to swipe through onboarding screens
    private lateinit var onboardingAdapter: OnboardingAdapter // Adapter for onboarding screens
    private lateinit var binding: ActivityOnboardingBinding // Binding object for activity layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout and set the root view to binding
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewPager and set up onboarding adapter
        viewPager = binding.viewPager
        onboardingAdapter = OnboardingAdapter(this)
        viewPager.adapter = onboardingAdapter // Sets the adapter for ViewPager

        // Set initial visibility for buttons
        binding.skipButton.visibility = View.VISIBLE
        binding.backButton.visibility = View.GONE
        binding.nextButton.visibility = View.VISIBLE
        binding.getStartedButton.visibility = View.GONE

        setUpButtons() // Setup button interactions
        setupProgressIndicator() // Initialize progress indicator
    }

    private fun setUpButtons() {
        // Set up the skip button to jump to HomeActivity
        binding.skipButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent) // Start HomeActivity when skip is pressed
            finish() // Close OnboardingActivity
        }

        // Set up next button to move to the next onboarding screen
        binding.nextButton.setOnClickListener {
            if (viewPager.currentItem < onboardingAdapter.itemCount - 1) {
                viewPager.currentItem += 1 // Move to the next page if possible
            }
        }

        // Set up back button to move to the previous onboarding screen
        binding.backButton.setOnClickListener {
            if (viewPager.currentItem > 0) {
                viewPager.currentItem -= 1 // Move to the previous page if possible
            }
        }

        // Set up get started button to jump to HomeActivity
        binding.getStartedButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent) // Start HomeActivity
            finish() // Close OnboardingActivity
        }

        // Monitor page changes in ViewPager to update button visibility and progress
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // Update button visibility based on the current page
                when (position) {
                    0 -> { // First page setup
                        binding.skipButton.visibility = View.VISIBLE
                        binding.backButton.visibility = View.GONE
                        binding.nextButton.visibility = View.VISIBLE
                        binding.getStartedButton.visibility = View.GONE
                    }
                    1 -> { // Middle page setup
                        binding.skipButton.visibility = View.GONE
                        binding.backButton.visibility = View.VISIBLE
                        binding.nextButton.visibility = View.VISIBLE
                        binding.getStartedButton.visibility = View.GONE
                    }
                    2 -> { // Last page setup
                        binding.skipButton.visibility = View.GONE
                        binding.backButton.visibility = View.VISIBLE
                        binding.nextButton.visibility = View.GONE
                        binding.getStartedButton.visibility = View.VISIBLE
                    }
                }

                // Call to animate the progress indicator as pages change
                animateProgressIndicator(position)
            }
        })
    }

    private fun setupProgressIndicator() {
        // Initialize progress indicator with maximum value and starting progress
        binding.progressIndicator.max = 100 // Max progress as percentage
        binding.progressIndicator.progress = 0 // Initial progress set to zero
    }

    private fun animateProgressIndicator(position: Int) {
        // Calculate target progress based on page position
        val targetProgress = (position + 1) * 100 / onboardingAdapter.itemCount

        // Animate the progress indicator to the target progress
        val animator = ValueAnimator.ofInt(binding.progressIndicator.progress, targetProgress)
        animator.duration = 300 // Animation duration in milliseconds
        animator.addUpdateListener { animation ->
            binding.progressIndicator.progress = animation.animatedValue as Int
        }
        animator.start() // Start the animation
    }
}
