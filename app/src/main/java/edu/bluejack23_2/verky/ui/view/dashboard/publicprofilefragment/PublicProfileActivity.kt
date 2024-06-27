package edu.bluejack23_2.verky.ui.view.dashboard.publicprofilefragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.databinding.ActivityPublicProfileBinding
import edu.bluejack23_2.verky.ui.view.dashboard.chatfragment.NotificationActivity
import edu.bluejack23_2.verky.ui.view.dashboard.profilefragment.AboutMeFragment
import edu.bluejack23_2.verky.ui.view.dashboard.profilefragment.MyPhotoFragment
import java.util.Calendar

class PublicProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublicProfileBinding
    private lateinit var myphotoFragment: PublicMyPhotoFragment
    private lateinit var aboutMeFragment: PublicAboutMeFragment

    private fun init() {
        myphotoFragment = PublicMyPhotoFragment()
        aboutMeFragment = PublicAboutMeFragment()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        binding = ActivityPublicProfileBinding.inflate(layoutInflater)
        binding.backButton.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        val user = intent.getParcelableExtra<User>("USER_DATA")

        if (user != null) {
            val bundle = Bundle()
            bundle.putParcelable("USER_DATA", user)
            aboutMeFragment.arguments = bundle
            myphotoFragment.arguments = bundle
        }

        if (user != null) {
            binding.ProfileName.text = user.name
            Glide.with(this)
                .load(user.profile_picture)
                .placeholder(R.color.gray_200)
                .into(binding.ProfilePicture)
        }

        supportFragmentManager
            .beginTransaction()
            .replace(binding.contentLayout.id, myphotoFragment)
            .runOnCommit { myphotoFragment.setContent(user) }
            .commit()

        setupRadioGroup()
        setContentView(binding.root)
    }

    private fun setupRadioGroup() {
        binding.contentRadioGroup.check((binding.myPhotosButton.id))
        binding.contentRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.myPhotosButton.id -> {
                    supportFragmentManager.beginTransaction().replace(
                        binding.contentLayout.id,
                        myphotoFragment
                    ).commit()

                }

                binding.aboutMeButton.id -> {
                    supportFragmentManager.beginTransaction().replace(
                        binding.contentLayout.id,
                        aboutMeFragment
                    ).commit()
                }
            }
        }

    }
}