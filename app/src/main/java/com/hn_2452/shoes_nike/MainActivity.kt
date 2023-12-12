package com.hn_2452.shoes_nike

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hn_2452.shoes_nike.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            AlertDialog.Builder(this)
                .setMessage("Ứng dụng của bạn sẽ không nhận được thông báo!")
                .setPositiveButton("Đóng") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private var _mBinding: ActivityMainBinding? = null
    private val mBinding get() = _mBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setupBottomNavigation()
        askNotificationPermission()

    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav).setupWithNavController(
            navController
        )
    }

    fun setupBottomNavigation(show: Boolean) {
        if (show) {
            mBinding.bottomNav.visibility = View.VISIBLE
        } else {
            mBinding.bottomNav.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _mBinding = null
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            for (fragment in supportFragmentManager.fragments) {
                val subFragments = fragment.childFragmentManager.fragments
                for (subFragment in subFragments) {
                    subFragment.onActivityResult(requestCode, resultCode, data)
                }
            }
        } catch (e: Exception) {
            Log.d("ERROR", e.toString())
        }
    }

}