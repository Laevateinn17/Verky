package edu.bluejack23_2.verky.ui.view.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.data.Resource
import edu.bluejack23_2.verky.data.model.User
import edu.bluejack23_2.verky.databinding.FragmentStep3RegisterBinding
import edu.bluejack23_2.verky.ui.adapter.GalleryAdapter
import edu.bluejack23_2.verky.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class Step3RegisterFragment : Fragment() {

    private var _binding: FragmentStep3RegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel : AuthViewModel

    interface OnContinueListener {
        fun goToFragmentRegist2()
        fun registerCompleted()
    }


    private var listener: OnContinueListener? = null
    private var user: User? = null


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
        _binding = FragmentStep3RegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        binding.backButton.setOnClickListener{
            listener?.goToFragmentRegist2()
        }

        user = arguments?.getParcelable("user")
        user?.let { authViewModel.setUser(it) }

        val recyclerView: RecyclerView = binding.ImageRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        val adapter = GalleryAdapter(this, authViewModel)
        recyclerView.adapter = adapter

        binding.registerButton.setOnClickListener {
            val images = authViewModel.images.value?.filterNotNull()
            if (!images.isNullOrEmpty()) {
                authViewModel.uploadImages(images,
                    onSuccess = { imageUrls ->
                        user?.profile_picture = imageUrls[0]
                        user?.gallery_picture = imageUrls
                        register()
                    },
                    onFailure = { errorMessage ->
                        Toast.makeText(
                            requireContext(),
                            "Failed to upload images: $errorMessage",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
            else{
                Toast.makeText(requireContext(), "Image must be uploaded minimum 1", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun register() {
        viewLifecycleOwner.lifecycleScope.launch {
            val password = arguments?.getString("password")
            if (password != null && user != null) {
                authViewModel.signUp(user!!.name, user!!.email, password)
                authViewModel.signUpFlow.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Handle loading state
                        }
                        is Resource.Success -> {
                            authViewModel.addUser(user = user!!,
                                onSuccess = {
                                    listener?.registerCompleted()
                                },
                                onFailure = { errorMessage ->
                                    Toast.makeText(
                                        requireContext(),
                                        "Error Registering the User",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                        is Resource.Failure -> {
                            Toast.makeText(requireContext(), "Error Registering the User", Toast.LENGTH_SHORT).show()
                        }

                        null -> Toast.makeText(requireContext(), "null!", Toast.LENGTH_SHORT)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val position = requestCode - GalleryAdapter.PICK_IMAGE_REQUEST
            if (position in (authViewModel.images.value?.indices ?: 0..0)) {
                data.data?.let { uri ->
                    authViewModel.updateImage(position, uri)
                }
            }
        }
    }


}