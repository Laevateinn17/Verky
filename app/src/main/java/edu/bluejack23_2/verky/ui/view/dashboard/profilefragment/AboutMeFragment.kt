package edu.bluejack23_2.verky.ui.view.dashboard.profilefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.data.model.LogUser
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.databinding.FragmentAboutMeBinding
import edu.bluejack23_2.verky.ui.adapter.InterestAdapter
import edu.bluejack23_2.verky.ui.adapter.MyPhotoAdapter
import edu.bluejack23_2.verky.ui.viewmodel.AuthViewModel
import edu.bluejack23_2.verky.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class AboutMeFragment : Fragment() {

    private var _binding: FragmentAboutMeBinding? = null
    private val binding get() = _binding!!
    private lateinit var interestAdapter: InterestAdapter
    private lateinit var userViewModel : UserViewModel
    private var interests = listOf("\uD83D\uDCDA Books",
        "\uD83C\uDFB5 Music",
        "⛰\uFE0F Adventure",
        "\uD83C\uDF7D\uFE0F Cuisine",
        "\uD83E\uDDD8\u200D♂\uFE0F Yoga",
        "\uD83D\uDDA5\uFE0F Technology",
        "\uD83D\uDCAA Health",
        "\uD83C\uDFA8 Art",
        "\uD83C\uDF0D Travel",
        "\uD83D\uDC3E Pets",
        "\uD83C\uDFCB\uFE0F Fitness",
        "\uD83C\uDFAE Gaming",
        "\uD83C\uDFA5 Movies",
        "\uD83D\uDCF7 Photography",
        "\uD83C\uDFAD Theater",
        "\uD83C\uDF3F Gardening",
        "\uD83C\uDF63 Cooking",
        "\uD83D\uDEB4\u200D♂\uFE0F Cycling",
        "\uD83C\uDFA7 Podcasts",
        "\uD83D\uDCD6 Reading",
        "\uD83C\uDFC3\u200D♀\uFE0F Running")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutMeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val interestLayoutManager = FlexboxLayoutManager(context)
        interestLayoutManager.flexDirection = FlexDirection.ROW
        interestLayoutManager.justifyContent = JustifyContent.FLEX_START

        interestAdapter = InterestAdapter(interests) { selectedInterests ->
           Log.e("selectedItems", selectedInterests.toString())
        }
        binding.interestRecyclerView.adapter = interestAdapter
        binding.interestRecyclerView.layoutManager = interestLayoutManager

        binding.updateButton.setOnClickListener{
            updateUser()
        }
        setContent(LogUser.getUser().value)
    }

    private fun updateUser() {

        val name = binding.nameET.text.toString()
        val day = binding.dayDOB.text.toString()
        val month = binding.dayMonth.text.toString()
        val year = binding.dayYear.text.toString()
        val gender = if (binding.MaleRB.isChecked) "Male" else "Female"
        val height = binding.heightEditText.text.toString().toIntOrNull() ?: 0
        val interests = interestAdapter.getSelectedItems()

        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Name can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (day.isEmpty() || month.isEmpty() || year.isEmpty()) {
            Toast.makeText(requireContext(), "DOB can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (!binding.MaleRB.isChecked && !binding.FemaleRB.isChecked) {
            Toast.makeText(requireContext(), "Please select gender", Toast.LENGTH_SHORT).show()
            return
        }

        if (height == 0) {
            Toast.makeText(requireContext(), "Height can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedUser = User(
            id = LoggedUser.getInstance().getUser()?.id ?: "",
            name = name,
            dob = "$year-$month-$day",
            gender = gender,
            height = height,
            interest = interests
        )

        lifecycleScope.launch {
            userViewModel.updateUser(updatedUser)
            Toast.makeText(requireContext(), "User updated", Toast.LENGTH_SHORT).show()
        }
    }

    fun setContent(user: User?) {
        user?.let {
            binding.nameET.setText(it.name)

            val dobParts = it.dob.split("-")
            if (dobParts.size == 3) {
                val year = dobParts[0].toInt()
                val month = dobParts[1].toInt()
                val day = dobParts[2].toInt()

                val cal = Calendar.getInstance()
                cal.set(year, month - 1, day)

                binding.dayDOB.setText(day.toString())
                binding.dayMonth.setText(month.toString())
                binding.dayYear.setText(year.toString())
            }

            if (it.gender == "Male") {
                binding.MaleRB.isChecked = true
            } else if(it.gender == "Female") {
                binding.FemaleRB.isChecked = true
            }
            binding.heightEditText.setText(it.height.toString())
            Log.e("interestAdapter", it.interest.toString())

            interestAdapter.setSelectedItems(it.interest)
        }
    }
}
