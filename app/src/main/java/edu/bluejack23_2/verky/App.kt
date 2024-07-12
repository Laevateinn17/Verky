package edu.bluejack23_2.verky

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        applyDarkModeSetting()
    }

    private fun applyDarkModeSetting() {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false)
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}