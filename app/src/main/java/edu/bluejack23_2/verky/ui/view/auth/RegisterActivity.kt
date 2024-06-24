package edu.bluejack23_2.verky.ui.view.auth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.databinding.ActivityRegisterBinding
import edu.bluejack23_2.verky.databinding.FragmentStep1RegisterBinding

class RegisterActivity : AppCompatActivity(), Step1RegisterFragment.OnContinueListener {

    private lateinit var binding : ActivityRegisterBinding;

    private lateinit var step1Fragment : Step1RegisterFragment
    private lateinit var step2Fragment : Step2RegisterFragment
    private lateinit var step3Fragment : Step1RegisterFragment

    private lateinit var step1FragmentBinding : FragmentStep1RegisterBinding

    fun init() {
        step1Fragment = Step1RegisterFragment()
        step2Fragment = Step2RegisterFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        supportFragmentManager.beginTransaction().replace(R.id.registerFragmentContainer, step1Fragment).addToBackStack(null).commit()

//        step1FragmentBinding.continueButton.setOnClickListener{
//            supportFragmentManager.beginTransaction().replace(R.id.registerFragmentContainer, step2Fragment).addToBackStack(null).commit()
//        }

    }

    override fun goToFragmentRegist2() {
        supportFragmentManager.beginTransaction().replace(R.id.registerFragmentContainer, step2Fragment).addToBackStack(null).commit()
    }


}