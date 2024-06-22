package edu.bluejack23_2.verky.ui.view.dashboard.profilefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.databinding.FragmentAboutMeBinding
import java.util.Calendar
import java.util.Date

class AboutMeFragment : Fragment() {

    private var _binding: FragmentAboutMeBinding? = null
    private val binding get() = _binding!!

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
    }

    fun setContent(user: User?) {
        user?.let {
            binding.nameET.setText(it.name)

            val cal = Calendar.getInstance()
            cal.time = it.dob

            val day = cal.get(Calendar.DAY_OF_MONTH)
            val month = cal.get(Calendar.MONTH) + 1
            val year = cal.get(Calendar.YEAR)

            binding.dayDOB.setText(day.toString())
            binding.dayMonth.setText(month.toString())
            binding.dayYear.setText(year.toString())

            if (it.gender == "Male") {
                binding.MaleRB.isChecked = true
            } else if(it.gender == "Female") {
                binding.FemaleRB.isChecked = true
            }
        }
    }
}
