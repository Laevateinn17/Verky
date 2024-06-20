package edu.bluejack23_2.verky.ui.view.dashboard.profilefragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.databinding.FragmentProfileBinding
import edu.bluejack23_2.verky.ui.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadProfile();

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        setupObservers(binding.root);
        setupRadioGroup()

        binding.settingsButton.setOnClickListener{
            startActivity(Intent(this.activity, SettingsActivity::class.java))
        }

        return binding.root;
    }

    private fun setupObservers(view: View) {
        viewModel.profileData.observe(viewLifecycleOwner) { profile ->
            Log.e("test", profile.name)
            view.findViewById<TextView>(R.id.ProfileName).text = profile.name
            val profileImageView = view.findViewById<ImageView>(R.id.ProfilePicture)
            Glide.with(this)
                .load(profile.profilePicture)
                .placeholder(R.drawable.custom_button) //nanti ganti abu"
                .into(profileImageView)
        }
    }

    private fun setupRadioGroup() {
        val radioGroup = binding.contentRadioGroup
        Log.e("radioButton", radioGroup.childCount.toString())
        for (i in 0 until radioGroup.childCount) {
            val radioButton = radioGroup.getChildAt(i) as RadioButton
            Log.e("radioButton", radioButton.toString())

        }
        binding.contentRadioGroup.setOnCheckedChangeListener{ _, checkedId ->
            Log.e("radio button", checkedId.toString())
            when (checkedId) {
                binding.myPhotosButton.id -> {
                    childFragmentManager.beginTransaction().replace(binding.contentLayout.id,
                        MyPhotoFragment()
                    ).commit()

                }
                binding.aboutMeButton.id -> {
                    childFragmentManager.beginTransaction().replace(binding.contentLayout.id,
                        AboutMeFragment()
                    ).commit()
                }
            }
        }
        binding.contentRadioGroup.check((binding.myPhotosButton.id))
    }
}