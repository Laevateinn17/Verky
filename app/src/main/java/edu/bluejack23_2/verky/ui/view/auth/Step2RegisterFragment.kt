package edu.bluejack23_2.verky.ui.view.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.bluejack23_2.verky.databinding.FragmentStep2RegisterBinding

class Step2RegisterFragment : Fragment() {


    private var _binding: FragmentStep2RegisterBinding? = null
    private val binding get() = _binding!!


    private var listener: Step2RegisterFragment.OnContinueListener? = null

    interface OnContinueListener {
        fun goToFragmentRegist3()
        fun goToFragmentRegist1()
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
        _binding = FragmentStep2RegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.continueButton.setOnClickListener {
            listener?.goToFragmentRegist3()
        }

        binding.backButton.setOnClickListener{
            listener?.goToFragmentRegist1()
        }
    }
}