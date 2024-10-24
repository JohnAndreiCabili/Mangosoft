package com.example.mangosoft

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.mangosoft.databinding.ActivityOnboardingBinding
import com.google.android.material.progressindicator.LinearProgressIndicator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var onboardingAdapter: OnboardingAdapter
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager
        onboardingAdapter = OnboardingAdapter(this)
        viewPager.adapter = onboardingAdapter

        binding.skipButton.visibility = View.VISIBLE
        binding.backButton.visibility = View.GONE
        binding.nextButton.visibility = View.VISIBLE
        binding.getStartedButton.visibility = View.GONE

        setUpButtons()
        setupProgressIndicator()
    }

    private fun setUpButtons() {
        binding.skipButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.nextButton.setOnClickListener {
            if (viewPager.currentItem < onboardingAdapter.itemCount - 1) {
                viewPager.currentItem += 1
            }
        }

        binding.backButton.setOnClickListener {
            if (viewPager.currentItem > 0) {
                viewPager.currentItem -= 1
            }
        }

        binding.getStartedButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when (position) {
                    0 -> {
                        binding.skipButton.visibility = View.VISIBLE
                        binding.backButton.visibility = View.GONE
                        binding.nextButton.visibility = View.VISIBLE
                        binding.getStartedButton.visibility = View.GONE
                    }
                    1 -> {
                        binding.skipButton.visibility = View.GONE
                        binding.backButton.visibility = View.VISIBLE
                        binding.nextButton.visibility = View.VISIBLE
                        binding.getStartedButton.visibility = View.GONE
                    }
                    2 -> {
                        binding.skipButton.visibility = View.GONE
                        binding.backButton.visibility = View.VISIBLE
                        binding.nextButton.visibility = View.GONE
                        binding.getStartedButton.visibility = View.VISIBLE
                    }
                }

                // Animate progress indicator based on the current position
                animateProgressIndicator(position)
            }
        })
    }

    private fun setupProgressIndicator() {
        binding.progressIndicator.max = 100
        binding.progressIndicator.progress = 0
    }

    private fun animateProgressIndicator(position: Int) {
        val targetProgress = (position + 1) * 100 / onboardingAdapter.itemCount

        val animator = ValueAnimator.ofInt(binding.progressIndicator.progress, targetProgress)
        animator.duration = 300 // Duration of the animation
        animator.addUpdateListener { animation ->
            binding.progressIndicator.progress = animation.animatedValue as Int
        }
        animator.start()
    }
}
