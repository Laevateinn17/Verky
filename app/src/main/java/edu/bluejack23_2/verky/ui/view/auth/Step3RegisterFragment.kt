package edu.bluejack23_2.verky.ui.view.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.bluejack23_2.verky.databinding.FragmentStep3RegisterBinding

class Step3RegisterFragment : Fragment() {

    private var _binding: FragmentStep3RegisterBinding? = null
    private val binding get() = _binding!!

    interface OnContinueListener {
        fun goToFragmentRegist2()
    }


    private var listener: Step3RegisterFragment.OnContinueListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Step3RegisterFragment.OnContinueListener) {
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

        binding.backButton.setOnClickListener{
            listener?.goToFragmentRegist2()
        }
    }


}