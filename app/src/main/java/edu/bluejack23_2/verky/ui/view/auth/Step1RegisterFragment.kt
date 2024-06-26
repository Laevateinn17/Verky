package edu.bluejack23_2.verky.ui.view.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.databinding.FragmentStep1RegisterBinding
import edu.bluejack23_2.verky.ui.viewmodel.AuthViewModel

@AndroidEntryPoint
class Step1RegisterFragment : Fragment() {

    private var _binding: FragmentStep1RegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel : AuthViewModel
    private var listener: OnContinueListener? = null

    interface OnContinueListener {
        fun goToFragmentRegist2()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnContinueListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnContinueListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep1RegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        validate()
    }

    fun validate(){
        binding.continueButton.setOnClickListener {
            val name = binding.registerNameInput.text.toString().trim()
            val day = binding.registerDayInput.text.toString().trim()
            val month = binding.registerMonthInput.text.toString().trim()
            val year = binding.registerYearInput.text.toString().trim()
            val email = binding.registerEmailInput.text.toString().trim()
            val password = binding.registerPasswordInput.text.toString().trim()

            if (validateInputs(name, day, month, year, email, password)) {
                val user = User(name = name, email = email, dob = "$day-$month-$year")
                authViewModel.setUser(user)
                listener?.goToFragmentRegist2()
            }
        }
    }


    private fun validateInputs(name: String, day: String, month: String, year: String, email: String, password: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Name is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (day.isEmpty() || month.isEmpty() || year.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter valid date of birth", Toast.LENGTH_SHORT).show()
            return false
        }

        val dayInt = day.toIntOrNull()
        val monthInt = month.toIntOrNull()
        val yearInt = year.toIntOrNull()

        if (dayInt == null || monthInt == null || yearInt == null) {
            Toast.makeText(requireContext(), "Invalid date format", Toast.LENGTH_SHORT).show()
            return false
        }

        if (dayInt < 1 || dayInt > 31 || monthInt < 1 || monthInt > 12 || yearInt < 1900 || yearInt > 2024) {
            Toast.makeText(requireContext(), "Invalid date range", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Invalid email address", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(requireContext(), "Password is empty", Toast.LENGTH_SHORT).show()
            return false
        }

        val alphanumericRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$")
        if (!alphanumericRegex.matches(password)) {
            Toast.makeText(requireContext(), "Password must contain at least one letter and one number", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


}