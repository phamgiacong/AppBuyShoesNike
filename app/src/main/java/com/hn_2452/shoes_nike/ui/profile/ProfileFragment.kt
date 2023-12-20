package com.hn_2452.shoes_nike.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import coil.load
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    companion object {
        const val TAG = "Nike:ProfileFragment: "
    }

    private val mProfileFragmentViewModel: ProfileFragmentViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding?.editProfile?.setOnClickListener {
            mNavController?.navigate(R.id.changeInfoFragment)
        }

        mBinding?.changeAddress?.setOnClickListener {
            mNavController?.navigate(R.id.manageAddressFragment)
        }

        mBinding?.settingNotification?.setOnClickListener {
            mNavController?.navigate(R.id.manageNotificationFragment)
        }

        mBinding?.managePayment?.setOnClickListener {
            mNavController?.navigate(R.id.managePaymentFragment)
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
                mBinding?.imvUser?.load(users[0].avatar) {
                    error(R.drawable.ic_launcher_background)
                }
                mBinding?.tvUserName?.text = users[0].name
                if(users[0].phoneNumber != null) {
                    mBinding?.tvPhoneNumber?.text = users[0].phoneNumber
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

}