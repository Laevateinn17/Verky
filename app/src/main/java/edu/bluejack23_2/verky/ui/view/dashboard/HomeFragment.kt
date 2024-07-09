package edu.bluejack23_2.verky.ui.view.dashboard

import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.ui.viewmodel.HomeViewModel
import edu.bluejack23_2.verky.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }
    private lateinit var nameAgeTextView: TextView
    private lateinit var userImageView: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        nameAgeTextView = view.findViewById(R.id.name_age_text) // Initialize TextView
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {
            try {
                val user = userViewModel.getPotentialMatch()
                user?.let {
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val birthDate = sdf.parse(it.dob)

                    val birthCalendar = Calendar.getInstance()
                    birthCalendar.time = birthDate

                    val today = Calendar.getInstance()
                    var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
                    val displayNameAge = "${it.name}, ${age}"
                    nameAgeTextView.text = displayNameAge
                    Log.e("imageURI", user.gallery_picture.get(0))
                    userImageView.setImageURI(Uri.parse(user.gallery_picture.get(0)))

                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("errrorrr", e.toString())
                Toast.makeText(context, "Failed to fetch potential match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}