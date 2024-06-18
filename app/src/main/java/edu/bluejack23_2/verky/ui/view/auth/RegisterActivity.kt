package edu.bluejack23_2.verky.ui.view.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding;

    private lateinit var step1Fragment : Step1RegisterFragment
    private lateinit var step2Fragment : Step1RegisterFragment
    private lateinit var step3Fragment : Step1RegisterFragment

    fun init() {
        step1Fragment = Step1RegisterFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        supportFragmentManager.beginTransaction().replace(R.id.registerFragmentContainer, step1Fragment).commit()


    }


}