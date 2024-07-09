package edu.bluejack23_2.verky.ui.view.dashboard

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.LogUser
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
        LogUser.getUser().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                lifecycleScope.launch {
                    try {
                        val user = userViewModel.getPotentialMatch()
                        user?.let {
                            val sdf = SimpleDateFormat("yyyy-MM-dd")
                            val birthDate = sdf.parse(it.dob)

                            val birthCalendar = Calendar.getInstance()
                            birthCalendar.time = birthDate

                            val today = Calendar.getInstance()
                            val age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
                            val displayNameAge = "${it.name}, $age"
                            nameAgeTextView.text = displayNameAge
                            val backgroundProfile =
                                view.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.BackgroundProfile)
                            Glide.with(this@HomeFragment)
                                .asBitmap()
                                .load(it.gallery_picture[0])
                                .placeholder(R.color.gray_200)
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        val bitmapDrawable = BitmapDrawable(resources, resource)
                                        backgroundProfile.background = bitmapDrawable
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {
                                        // Handle the placeholder if needed
                                    }
                                })
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("errrorrr", e.toString())
                        Toast.makeText(context, "Failed to fetch potential match", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}