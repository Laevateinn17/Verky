package edu.bluejack23_2.verky.ui.view.auth

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

    private lateinit var binding : FragmentStep1RegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentStep1RegisterBinding.inflate(layoutInflater)



        return binding.root
    }


}