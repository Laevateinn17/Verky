package edu.bluejack23_2.verky.ui.view.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.databinding.ActivityPublicProfileBinding
import edu.bluejack23_2.verky.ui.view.dashboard.chatfragment.NotificationActivity

class PublicProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPublicProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublicProfileBinding.inflate(layoutInflater)
        binding.backButton.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        val user = intent.getParcelableExtra<User>("USER_DATA")
        if (user != null) {
            binding.ProfileName.text = user.name
            Glide.with(this)
                .load(user.profile_picture)
                .placeholder(R.color.gray_200)
                .into(binding.ProfilePicture)
        }

        setContentView(binding.root)
    }
}