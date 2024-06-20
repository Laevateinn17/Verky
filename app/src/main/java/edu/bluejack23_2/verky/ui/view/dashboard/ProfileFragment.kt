package edu.bluejack23_2.verky.ui.view.dashboard

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.ui.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadProfile();
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        setupObservers(view);
        return view;
    }

    private fun setupObservers(view: View) {
        viewModel.profileData.observe(viewLifecycleOwner) { profile ->
            view.findViewById<EditText>(R.id.nameEditText).setText(profile.name)
            view.findViewById<TextView>(R.id.ProfileName).text = profile.name
            val profileImageView = view.findViewById<ImageView>(R.id.ProfilePicture)
            Glide.with(this)
                .load(profile.profilePicture)
                .placeholder(R.drawable.custom_button) //nanti ganti abu"
                .into(profileImageView)
        }
    }
}