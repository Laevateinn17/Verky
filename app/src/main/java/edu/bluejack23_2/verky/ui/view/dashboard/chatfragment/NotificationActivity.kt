package edu.bluejack23_2.verky.ui.view.dashboard.chatfragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.databinding.ActivityNotificationBinding
import edu.bluejack23_2.verky.ui.adapter.NotificationAdapter
import edu.bluejack23_2.verky.ui.view.dashboard.DashboardActivity
import edu.bluejack23_2.verky.ui.viewmodel.AuthViewModel
import edu.bluejack23_2.verky.ui.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNotificationBinding
    private lateinit var notificationAdapter : NotificationAdapter
    private val viewModel: NotificationViewModel by lazy {
        ViewModelProvider(this).get(NotificationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        setContentView(binding.root)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter(listOf())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = notificationAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.notificationList.observe(this) { notifications ->
            notifications?.let {
                notificationAdapter.updateData(it)
            }
        }
    }
}