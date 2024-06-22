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
    private lateinit var myphotoFragment : MyPhotoFragment
    private lateinit var aboutMeFragment: AboutMeFragment

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()

    private fun init(){
        myphotoFragment = MyPhotoFragment()
        aboutMeFragment = AboutMeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadProfile();
        init();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            view.findViewById<TextView>(R.id.ProfileName).text = profile.name

            val profileImageView = view.findViewById<ImageView>(R.id.ProfilePicture)
            Glide.with(this)
                .load(profile.profilePicture)
                .placeholder(R.color.gray)
                .into(profileImageView)


            if (aboutMeFragment.isAdded && aboutMeFragment.view != null) {
                aboutMeFragment.setContent(profile)
            } else {
                childFragmentManager
                    .beginTransaction()
                    .replace(binding.contentLayout.id, aboutMeFragment)
                    .runOnCommit { aboutMeFragment.setContent(profile) }
                    .commit()
            }
            childFragmentManager.beginTransaction().replace(binding.contentLayout.id, myphotoFragment).commit()
        }
    }

    private fun setupRadioGroup() {
        binding.contentRadioGroup.check((binding.myPhotosButton.id))
        binding.contentRadioGroup.setOnCheckedChangeListener{ _, checkedId ->
            when (checkedId) {
                binding.myPhotosButton.id -> {
                    childFragmentManager.beginTransaction().replace(binding.contentLayout.id,
                        myphotoFragment
                    ).commit()

                }
                binding.aboutMeButton.id -> {
                    childFragmentManager.beginTransaction().replace(binding.contentLayout.id,
                        aboutMeFragment
                    ).commit()
                }
            }
        }

    }
}