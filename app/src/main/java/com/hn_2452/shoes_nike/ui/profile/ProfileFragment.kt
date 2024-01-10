package com.hn_2452.shoes_nike.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.viewModels
import coil.load
import com.hn_2452.shoes_nike.BASE_URL
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    companion object {
        const val TAG = "Nike:ProfileFragment: "
    }

    private val mProfileFragmentViewModel: ProfileFragmentViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)

    override fun onStart() {
        super.onStart()
        setupBottomBar(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomBar(true)

        mBinding?.editProfile?.setOnClickListener {
            mNavController?.navigate(R.id.changeInfoFragment)
        }

        mBinding?.settingNotification?.setOnClickListener {
            mNavController?.navigate(R.id.notificationSettingFragment)
        }

        mBinding?.managePayment?.setOnClickListener {
            Toast.makeText(requireContext(), "Chức năng hiện đang phát triển", Toast.LENGTH_SHORT).show()
        }

        mBinding?.security?.setOnClickListener {
            mNavController?.navigate(R.id.changePasswordFragment)
        }

        mBinding?.logout?.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setNegativeButton("Hủy"
                ) { dialog, _ ->
                    dialog?.dismiss()
                }
                .setPositiveButton("Đăng xuất"
                ) { dialog, _ ->
                    logout()
                    dialog?.dismiss()
                }
            dialog.show()
        }

        mBinding?.btnSignIn?.setOnClickListener {
            mNavController?.navigate(R.id.loginFragment)
        }

        setupUserInfo()
    }

    private fun setupUserInfo() {
        mProfileFragmentViewModel.mUsers.observe(viewLifecycleOwner) { users ->
            if(users != null && users.isNotEmpty()) {
                val user = users[0]

                if(user.accountType != 0) {
                    mBinding?.security?.visibility = View.GONE
                }  else {
                    mBinding?.security?.visibility = View.VISIBLE
                }

                mBinding?.imvUser?.load(user.avatar) {
                    error(R.drawable.user_placeholder)
                }

                mBinding?.tvUserName?.text = user.name
                if(user.phoneNumber != null) {
                    mBinding?.tvPhoneNumber?.text = user.phoneNumber
                    mBinding?.tvPhoneNumber?.visibility = View.VISIBLE
                } else {
                    mBinding?.tvPhoneNumber?.visibility = View.GONE
                }
                mBinding?.layoutNeedLogin?.visibility = View.GONE
                mBinding?.mainLayout?.visibility = View.VISIBLE
            } else {
                Log.i(TAG, "setupUserInfo: user is null")
                mBinding?.mainLayout?.visibility = View.GONE
                mBinding?.layoutNeedLogin?.visibility = View.VISIBLE
            }
        }
    }

    private fun logout() {
        mProfileFragmentViewModel.logout().observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    Toast.makeText(requireContext(), "Đăng xuất thành công", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Log.e(TAG, "logout: fail")
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        setupBottomBar(false)
    }

}