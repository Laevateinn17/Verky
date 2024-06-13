package edu.bluejack23_2.verky.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.bluejack23_2.verky.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDashboardBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}