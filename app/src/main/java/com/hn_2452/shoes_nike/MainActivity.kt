package com.hn_2452.shoes_nike

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.hn_2452.shoes_nike.databinding.ActivityMainBinding
import com.hn_2452.shoes_nike.ui.home.HomeFragmentDirections
import com.hn_2452.shoes_nike.ui.home.HomeViewModel
import com.hn_2452.shoes_nike.ui.notification.TokenUserViewModel
import com.hn_2452.shoes_nike.ui.orders.OrderFragmentDirections
import com.hn_2452.shoes_nike.ui.splash_activity.SplashActivity
import com.hn_2452.shoes_nike.ui.splash_activity.SplashActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPaySDK
import kotlin.math.log


@AndroidEntryPoint
open class MainActivity : AppCompatActivity() {
    private val mHomeViewModel: HomeViewModel by viewModels()
    private val tokenUserViewModel :TokenUserViewModel by viewModels()
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
        val receivedBundle = intent.extras
        val id = receivedBundle?.getString("id")
        val type = receivedBundle?.getString("type")
        setContentView(mBinding.root)
        setupBottomNavigation(id, type)
        askNotificationPermission()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        // ZaloPay SDK Init
        ZaloPaySDK.init(553, Environment.SANDBOX)
        sendRegistrationToServer()

    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

    }

    private fun setupBottomNavigation(id:String? ,type:String?) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav).setupWithNavController(
            navController
        )
        if(type=="1"||type=="2"){
            id?.let { navController.navigate(HomeFragmentDirections.actionHomeFragmentToOrderDetailFragment(id)) }
        }
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
    private fun sendRegistrationToServer() {
        mHomeViewModel.mUsers.observe(this){users ->
            if(users != null && users.isNotEmpty()){
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }
                    val token = task.result
                    tokenUserViewModel.sendRegistrationToServer(token,users[0].id).observe(this){
                        if(it.data == true){
                            Log.e("TAG", "sendRegistrationToServer: success")
                        }
                    }
                })
            }else{
                Log.i("TAG", "updateUserInfo: user is null")
            }
        }
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }

}