package edu.bluejack23_2.verky.ui.view.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.bluejack23_2.verky.R
import edu.bluejack23_2.verky.databinding.ActivityDashboardBinding
import edu.bluejack23_2.verky.ui.view.dashboard.chatfragment.ChatFragment
import edu.bluejack23_2.verky.ui.view.dashboard.profilefragment.ProfileFragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDashboardBinding;
    private lateinit var chatFragment : ChatFragment;
    private lateinit var homeFragment : HomeFragment;
    private lateinit var profileFragment : ProfileFragment;
    private lateinit var bottomNavBar : BottomNavigationView;

    fun init(){
        chatFragment = ChatFragment();
        homeFragment = HomeFragment();
        profileFragment = ProfileFragment();
        bottomNavBar = binding.bottomNavBar;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init();

        supportFragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit()
        bottomNavBar.selectedItemId = R.id.action_home
        fragmentPage();
    }

    private fun fragmentPage() {
        bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    switchFragment(homeFragment)
                    true
                }
                R.id.action_chat -> {
                    switchFragment(chatFragment)
                    true
                }
                R.id.action_profile -> {
                    switchFragment(profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}