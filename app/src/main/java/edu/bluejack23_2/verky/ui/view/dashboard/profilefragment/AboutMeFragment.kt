package edu.bluejack23_2.verky.ui.view.dashboard.profilefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.data.model.LoggedUser
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.ui.viewmodel.ProfileViewModel


class AboutMeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_me, container, false)
    }

    fun setContent(user : User){
        if (user != null) {
            view?.findViewById<EditText>(R.id.nameInput)?.setText(user.name)
            view?.findViewById<EditText>(R.id.dayDOB)?.setText(user.dob.day)
            view?.findViewById<EditText>(R.id.dayMonth)?.setText(user.dob.month)
            view?.findViewById<EditText>(R.id.dayYear)?.setText(user.dob.year)
        }
    }

}