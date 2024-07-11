package edu.bluejack23_2.verky.ui.view.dashboard.profilefragment

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.databinding.ActivitySettingsBinding
import edu.bluejack23_2.verky.ui.view.auth.LoginActivity
import edu.bluejack23_2.verky.ui.viewmodel.AuthViewModel

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    lateinit var binding : ActivitySettingsBinding
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutButton.setOnClickListener{
            viewModel.logOut();
            val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.backButton.setOnClickListener{
            onBackPressed();
        }

        setupDarkModeSwitch()
    }

    private fun setupDarkModeSwitch() {
        binding.darkModeSwitch.isChecked = isDarkModeEnabled()

        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableDarkMode()
            } else {
                disableDarkMode()
            }
        }
    }

    private fun isDarkModeEnabled(): Boolean {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("dark_mode", false)
    }

    private fun enableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        saveDarkModeSetting(true)
    }

    private fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        saveDarkModeSetting(false)
    }

    private fun saveDarkModeSetting(isEnabled: Boolean) {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("dark_mode", isEnabled)
            apply()
        }
    }
}