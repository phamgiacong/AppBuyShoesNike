package com.hn_2452.shoes_nike

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hn_2452.shoes_nike.databinding.ActivityMainBinding

open class MainActivity : AppCompatActivity() {

    private var _mBinding: ActivityMainBinding? = null
    private val mBinding get() = _mBinding!!
    private var isBottomNavHidden = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setupBottomNavigation()

    }

    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav).setupWithNavController(
            navController
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }


    fun hideBottomNav() {
        if (!isBottomNavHidden) {
            _mBinding?.bottomNav?.visibility = View.GONE
            isBottomNavHidden = true
        }
    }

    fun showBottomNav() {
        if (isBottomNavHidden) {
            _mBinding?.bottomNav?.visibility = View.VISIBLE
            isBottomNavHidden = false
        }
    }

}