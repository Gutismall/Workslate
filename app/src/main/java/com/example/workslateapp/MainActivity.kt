package com.example.workslateapp


import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.example.workslateapp.Fragments.AccountFragment
import com.example.workslateapp.Fragments.ConstraintsFragment
import com.example.workslateapp.Fragments.LogFragment
import com.example.workslateapp.Fragments.MainFragment
import com.example.workslateapp.Fragments.MessagesFragment
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
        val mainFragment = MainFragment()
        val constraintsFragment = ConstraintsFragment()
        val logFragment = LogFragment()
        val messagesFragment = MessagesFragment()
        val accountFragment = AccountFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.Main_Fragment, mainFragment, "MainFragment")
            .add(R.id.Main_Fragment, constraintsFragment, "ConstraintsFragment")
            .add(R.id.Main_Fragment, logFragment, "LogFragment")
            .add(R.id.Main_Fragment, messagesFragment, "MassagesFragment")
            .add(R.id.Main_Fragment, accountFragment, "AccountFragment")
            .hide(constraintsFragment)
            .hide(logFragment)
            .hide(messagesFragment)
            .hide(accountFragment)
            .commit()

        binding.MainBottomBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> showFragment(mainFragment)
                R.id.navigation_constraints -> showFragment(constraintsFragment)
                R.id.navigation_log -> showFragment(logFragment)
                R.id.navigation_Massages -> showFragment(messagesFragment)
            }
            true
        }

        binding.MainTopBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.topNav_account -> {
                    showFragment(accountFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            supportFragmentManager.fragments.forEach { hide(it) }
            addToBackStack(null)
            show(fragment)
            commit()
        }
    }


}