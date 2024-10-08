package edu.bluejack23_2.verky.ui.view.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.LogUser
import edu.bluejack23_2.verky.data.model.Notification
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(this).get(UserViewModel::class.java)
    }
    private lateinit var nameAgeTextView: TextView
    private lateinit var imageBackground : ImageView
    private lateinit var acceptButton : ImageView
    private lateinit var declineButton : ImageView
    private lateinit var curruser : User;
    private lateinit var noMatchText: TextView
    private lateinit var matchProfileContainer: ConstraintLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        nameAgeTextView = view.findViewById(R.id.name_age_text)
        imageBackground = view.findViewById(R.id.backgroundImageView)
        acceptButton = view.findViewById(R.id.acceptButton)
        declineButton = view.findViewById(R.id.rejectButton)
        noMatchText = view.findViewById(R.id.no_match_text)
        matchProfileContainer = view.findViewById(R.id.match_profile_layout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUser.getUser().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                getPotentialMatch();
            }
        }

        acceptButton.setOnClickListener{
            acceptLogic();
        }

        declineButton.setOnClickListener {
            declineLogic();
        }
    }

    fun acceptLogic(){
        val notification = LogUser.getUser().value?.id?.let {
            curruser.id?.let { it1 ->
                Notification(
                    from = it,
                    to = it1,
                    timeStamp = System.currentTimeMillis()
                )
            }
        }
        lifecycleScope.launch {
            if (notification != null) {
                userViewModel.acceptUser(notification)
            }
        }
        getPotentialMatch()
    }

    fun declineLogic(){
        val notification = LogUser.getUser().value?.id?.let {
            curruser.id?.let { it1 ->
                Notification(
                    from = it,
                    to = it1,
                    timeStamp = System.currentTimeMillis()
                )
            }
        }
        lifecycleScope.launch {
            if (notification != null) {
                userViewModel.declineUser(notification)
            }
        }
        getPotentialMatch()
    }

    fun getPotentialMatch(){
        lifecycleScope.launch {
            try {
                curruser = userViewModel.getPotentialMatch()
                curruser?.let {
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val birthDate = sdf.parse(it.dob)

                    val birthCalendar = Calendar.getInstance()
                    birthCalendar.time = birthDate

                    val today = Calendar.getInstance()
                    val age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
                    val displayNameAge = "${it.name}, $age"
                    nameAgeTextView.text = displayNameAge

                    Glide.with(this@HomeFragment)
                        .load(it.gallery_picture[0])
                        .placeholder(R.color.gray_200)
                        .into(imageBackground)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                matchProfileContainer.visibility = View.GONE
                noMatchText.visibility = View.VISIBLE
//                Toast.makeText(context, "Failed to fetch potential match", Toast.LENGTH_SHORT).show()
            }
        }
    }
}