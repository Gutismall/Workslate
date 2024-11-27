package com.example.workslateapp


import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.example.workslateapp.Fragments.AccountFragment
import com.example.workslateapp.Fragments.ConstraintsFragment
import com.example.workslateapp.Fragments.LogFragment
import com.example.workslateapp.Fragments.MainFragment
import com.example.workslateapp.Fragments.MassagesFragment
import com.example.workslateapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

    }


    private fun initView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.Main_Fragment, MainFragment())
            .commit()
        binding.MainBottomBar.setOnNavigationItemSelectedListener { item ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_home -> { fragment = MainFragment() }
                R.id.navigation_constraints -> fragment = ConstraintsFragment()
                R.id.navigation_log -> fragment = LogFragment()
                R.id.navigation_Massages -> fragment = MassagesFragment()
            }
            loadFragment(fragment!!)
            true
        }
        binding.MainTopBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.topNav_account -> {
                    loadFragment(AccountFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.Main_Fragment, fragment)
        transaction.setCustomAnimations(
            android.R.anim.slide_in_left,  // Enter animation
            android.R.anim.slide_out_right, // Exit animation
            R.anim.fade_in, // Pop Enter animation (when coming back)
            R.anim.fade_out // Pop Exit animation (when going back)
        )
        transaction.commit()
    }


}