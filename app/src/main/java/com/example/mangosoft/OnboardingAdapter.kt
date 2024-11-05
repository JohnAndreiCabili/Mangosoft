package com.example.mangosoft

// Import statements for necessary classes and bindings
import android.content.Context // Provides access to app-specific resources and classes
import android.view.LayoutInflater // Allows layout resource files to be turned into View objects
import android.view.ViewGroup // Layout param class for holding View containers
import androidx.recyclerview.widget.RecyclerView // RecyclerView for efficient display and scrolling
import com.example.mangosoft.databinding.OnboardingScreen1Binding // Binding for first onboarding screen
import com.example.mangosoft.databinding.OnboardingScreen2Binding // Binding for second onboarding screen
import com.example.mangosoft.databinding.OnboardingScreen3Binding // Binding for third onboarding screen

class OnboardingAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // List of layout resources for each onboarding screen, specifying screen order
    private val layouts = listOf(
        R.layout.onboarding_screen1, // Layout for first onboarding screen
        R.layout.onboarding_screen2, // Layout for second onboarding screen
        R.layout.onboarding_screen3  // Layout for third onboarding screen
    )

    // Creates appropriate ViewHolder for each onboarding screen based on the viewType (position)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                // Inflate and return ViewHolder for the first onboarding screen
                val binding = OnboardingScreen1Binding.inflate(LayoutInflater.from(context), parent, false)
                OnboardingViewHolder1(binding)
            }
            1 -> {
                // Inflate and return ViewHolder for the second onboarding screen
                val binding = OnboardingScreen2Binding.inflate(LayoutInflater.from(context), parent, false)
                OnboardingViewHolder2(binding)
            }
            else -> {
                // Inflate and return ViewHolder for the third onboarding screen
                val binding = OnboardingScreen3Binding.inflate(LayoutInflater.from(context), parent, false)
                OnboardingViewHolder3(binding)
            }
        }
    }

    // Called to bind data to the ViewHolder; currently, no specific data is being bound
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Placeholder for binding logic if needed in the future
    }

    // Returns the total number of onboarding screens
    override fun getItemCount(): Int = layouts.size

    // Determines the type of view to create based on position, ensuring each page has its unique layout
    override fun getItemViewType(position: Int): Int = position

    // ViewHolder class for the first onboarding screen, holds binding for screen 1
    class OnboardingViewHolder1(val binding: OnboardingScreen1Binding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder class for the second onboarding screen, holds binding for screen 2
    class OnboardingViewHolder2(val binding: OnboardingScreen2Binding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder class for the third onboarding screen, holds binding for screen 3
    class OnboardingViewHolder3(val binding: OnboardingScreen3Binding) : RecyclerView.ViewHolder(binding.root)
}
