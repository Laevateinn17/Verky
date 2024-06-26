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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.databinding.FragmentStep3RegisterBinding
import edu.bluejack23_2.verky.ui.adapter.GalleryAdapter
import edu.bluejack23_2.verky.ui.viewmodel.AuthViewModel

@AndroidEntryPoint
class Step3RegisterFragment : Fragment() {

    private var _binding: FragmentStep3RegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel : AuthViewModel

    interface OnContinueListener {
        fun goToFragmentRegist2()
    }


    private var listener: OnContinueListener? = null

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

        val recyclerView: RecyclerView = binding.ImageRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        val adapter = GalleryAdapter(this, authViewModel)
        recyclerView.adapter = adapter
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