package com.hn_2452.shoes_nike

import android.app.NotificationManager
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
import com.google.firebase.messaging.FirebaseMessaging
import com.hn_2452.shoes_nike.NavMenuDirections
import com.hn_2452.shoes_nike.databinding.ActivityMainBinding
import com.hn_2452.shoes_nike.ui.home.HomeViewModel
import com.hn_2452.shoes_nike.ui.notification.TokenUserViewModel
import com.hn_2452.shoes_nike.utility.handleResource
import dagger.hilt.android.AndroidEntryPoint
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPaySDK


@AndroidEntryPoint
open class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "Nike: MainActivity"
    }

    private val mHomeViewModel: HomeViewModel by viewModels()
    private val tokenUserViewModel: TokenUserViewModel by viewModels()
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
        handleNotification(intent.extras)
        setContentView(mBinding.root)
        setupBottomNavigation()
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

    private fun sendRegistrationToServer() {
        mHomeViewModel.mUsers.observe(this) { users ->
            if (users != null && users.isNotEmpty()) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }
                    val token = task.result
                    tokenUserViewModel.sendRegistrationToServer(token, users[0].id).observe(this) {
                        if (it.data == true) {
                            Log.i(TAG, "sendRegistrationToServer: success")
                        }
                    }
                })
            } else {
                Log.i(TAG, "updateUserInfo: user is null")
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
        handleNotification(intent?.extras)
    }

    private fun handleNotification(bundle: Bundle?) {
        bundle ?: return
        val type = bundle.getString(NOTIFY_TYPE)
        val objectId = bundle.getString(NOTIFY_OBJECT_ID)
        Log.i(TAG, "handleNotification: type=$type object_id=$objectId")

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        when (type) {
            ORDER_NOTIFY -> {
                objectId?.let {
                    navController.navigate(
                        NavMenuDirections.actionGlobalOrderDetailFragment(objectId)
                    )
                }

            }

            OFFER_NOTIFY -> {
                objectId?.let {
                    handleResource(
                        data = mHomeViewModel.getOfferById(objectId),
                        context = this,
                        lifecycleOwner = this,
                        onSuccess = {
                            it?.let {
                                navController.navigate(
                                    NavMenuDirections.actionGlobalOfferDetailFragment(it)
                                )
                            }
                        }
                    )
                }

            }

            NEW_SHOES_NOTIFY -> {
                objectId?.let {
                    navController.navigate(
                        NavMenuDirections.actionGlobalShoesFragment(objectId)
                    )
                }
            }

            OTHER_NOTIFY -> {
                Log.i(TAG, "handleNotification: other notify")
            }
        }


    }

}