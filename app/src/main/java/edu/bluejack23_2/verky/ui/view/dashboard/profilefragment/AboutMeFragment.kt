package edu.bluejack23_2.verky.ui.view.dashboard.profilefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
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
        var interests = ArrayList<String>()

        interests.addAll(listOf("\uD83D\uDCDA Books",
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
                "\uD83C\uDFC3\u200D♀\uFE0F Running"))

        val interestLayoutManager = FlexboxLayoutManager(context)
        interestLayoutManager.flexDirection = FlexDirection.ROW
        interestLayoutManager.justifyContent = JustifyContent.FLEX_START

        binding.interestRecyclerView.layoutManager = interestLayoutManager
        interestAdapter = InterestAdapter(interests, listOf(), {})
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
            binding.heightEditText.setText(it.height.toString())
            Log.e("interestAdapter", it.interest.toString())
            interestAdapter.setItems(it.interest)
        }
    }

}
