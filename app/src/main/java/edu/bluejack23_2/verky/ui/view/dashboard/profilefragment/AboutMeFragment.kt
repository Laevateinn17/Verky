package edu.bluejack23_2.verky.ui.view.dashboard.profilefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.databinding.FragmentAboutMeBinding
import edu.bluejack23_2.verky.ui.adapter.InterestAdapter
import edu.bluejack23_2.verky.ui.adapter.MyPhotoAdapter
import edu.bluejack23_2.verky.ui.viewmodel.AuthViewModel
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class AboutMeFragment : Fragment() {

    private var _binding: FragmentAboutMeBinding? = null
    private val binding get() = _binding!!
    private lateinit var interestAdapter: InterestAdapter
    private val authViewModel : AuthViewModel by viewModels()

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

        binding.interestRecyclerView.layoutManager = LinearLayoutManager(context)
        interestAdapter = InterestAdapter(listOf(), listOf(), {})
        binding.interestRecyclerView.adapter = interestAdapter

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

            interestAdapter.setItems(user!!.interest)
        }
    }

}
