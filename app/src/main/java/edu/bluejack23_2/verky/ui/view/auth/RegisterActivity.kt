package edu.bluejack23_2.verky.ui.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.databinding.ActivityRegisterBinding
import edu.bluejack23_2.verky.databinding.FragmentStep1RegisterBinding
import edu.bluejack23_2.verky.util.toast

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity(),
        Step1RegisterFragment.OnContinueListener,
        Step2RegisterFragment.OnContinueListener,
        Step3RegisterFragment.OnContinueListener {

    private lateinit var binding : ActivityRegisterBinding;

    private lateinit var step1Fragment : Step1RegisterFragment
    private lateinit var step2Fragment : Step2RegisterFragment
    private lateinit var step3Fragment : Step3RegisterFragment

    fun init() {
        step1Fragment = Step1RegisterFragment()
        step2Fragment = Step2RegisterFragment()
        step3Fragment = Step3RegisterFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        supportFragmentManager.beginTransaction().replace(R.id.registerFragmentContainer, step1Fragment).addToBackStack(null).commit()
    }

    override fun goToFragmentRegist2() {
        supportFragmentManager.beginTransaction().replace(R.id.registerFragmentContainer, step2Fragment).addToBackStack(null).commit()
    }

    override fun registerCompleted() {
        toast("Success creating a user")
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun goToFragmentRegist3(bundle: Bundle) {
        step3Fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.registerFragmentContainer, step3Fragment).addToBackStack(null).commit()
    }

    override fun goToFragmentRegist1() {
        supportFragmentManager.beginTransaction().replace(R.id.registerFragmentContainer, step1Fragment).addToBackStack(null).commit()
    }

    override fun goToFragmentRegist2(bundle: Bundle) {
        step2Fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.registerFragmentContainer, step2Fragment).addToBackStack(null).commit()
    }

}