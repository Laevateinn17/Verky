package edu.bluejack23_2.verky.ui.view.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.replace
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.databinding.FragmentStep1RegisterBinding

class Step1RegisterFragment : Fragment() {

    private var _binding: FragmentStep1RegisterBinding? = null
    private val binding get() = _binding!!


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        binding.continueButton.setOnClickListener {
            listener?.goToFragmentRegist2()
        }
    }



}