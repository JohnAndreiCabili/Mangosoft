package com.example.mangosoft

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mangosoft.databinding.OnboardingScreen1Binding
import com.example.mangosoft.databinding.OnboardingScreen2Binding
import com.example.mangosoft.databinding.OnboardingScreen3Binding

class OnboardingAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // List of layouts for the onboarding screens
    private val layouts = listOf(
        R.layout.onboarding_screen1,
        R.layout.onboarding_screen2,
        R.layout.onboarding_screen3
    )

    // Creates ViewHolder for each onboarding screen based on view type (position)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                // Inflate layout for the first onboarding screen
                val binding = OnboardingScreen1Binding.inflate(LayoutInflater.from(context), parent, false)
                OnboardingViewHolder1(binding)
            }
            1 -> {
                // Inflate layout for the second onboarding screen
                val binding = OnboardingScreen2Binding.inflate(LayoutInflater.from(context), parent, false)
                OnboardingViewHolder2(binding)
            }
            else -> {
                // Inflate layout for the third onboarding screen
                val binding = OnboardingScreen3Binding.inflate(LayoutInflater.from(context), parent, false)
                OnboardingViewHolder3(binding)
            }
        }
    }

    // Binds data to the ViewHolder; for now, there's no specific data to bind
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // No specific binding logic for now
    }

    // Returns the total number of onboarding screens
    override fun getItemCount(): Int = layouts.size

    // Determines the type of view to create based on the current position
    override fun getItemViewType(position: Int): Int = position

    // ViewHolder class for the first onboarding screen
    class OnboardingViewHolder1(val binding: OnboardingScreen1Binding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder class for the second onboarding screen
    class OnboardingViewHolder2(val binding: OnboardingScreen2Binding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder class for the third onboarding screen
    class OnboardingViewHolder3(val binding: OnboardingScreen3Binding) : RecyclerView.ViewHolder(binding.root)
}
