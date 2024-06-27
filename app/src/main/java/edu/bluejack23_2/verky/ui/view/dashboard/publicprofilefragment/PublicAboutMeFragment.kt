package edu.bluejack23_2.verky.ui.view.dashboard.publicprofilefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.databinding.FragmentAboutMeBinding
import edu.bluejack23_2.verky.databinding.FragmentPublicAboutMeBinding
import edu.bluejack23_2.verky.ui.adapter.InterestAdapter
import java.util.Calendar


class PublicAboutMeFragment : Fragment() {
    private var _binding: FragmentPublicAboutMeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPublicAboutMeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<User>("USER_DATA")?.let { user ->
            setContent(user)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setContent(user: User) {
        binding.publicName.text = user.name
        binding.publicBirthDate.text = user.dob
        binding.publicGender.text = user.gender
    }

}