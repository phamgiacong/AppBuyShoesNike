package com.hn_2452.shoes_nike.ui.profile.change_password

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentChangePasswordBinding
import com.hn_2452.shoes_nike.utility.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {

    companion object {
        const val TAG = "Nike:ChangePasswordFragment: "
    }

    private val mChangePasswordViewModel: ChangePasswordViewModel by viewModels()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentChangePasswordBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.materialToolbar)
        setupLoading(mBinding?.loadingProgress)
        setupChangePassword()
    }

    private fun setupChangePassword() {
        mBinding?.btnChangePassword?.setOnClickListener {
            mBinding?.tvError?.text = ""
            mChangePasswordViewModel.changePassword(
                mBinding?.edtCurrentPassword?.editText?.text.toString(),
                mBinding?.edtNewPassword?.editText?.text.toString(),
                mBinding?.edtReNewPassword?.editText?.text.toString(),
            ).observe(viewLifecycleOwner) { result ->
                when(result?.status) {
                    Status.LOADING -> {
                        Log.i(TAG, "setupChangePassword: loading...")
                        startLoading()
                    }
                    Status.SUCCESS -> {
                        Log.i(TAG, "setupChangePassword: success ${result.data}")
                        stopLoading()
                        Toast.makeText(requireContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                        mNavController?.popBackStack()
                    }
                    Status.ERROR -> {
                        Log.e(TAG, "setupChangePassword: error ${result.message}")
                        stopLoading()
                        mBinding?.tvError?.text = result.message
                    }
                    null -> {
                        Log.i(TAG, "setupChangePassword: null")
                        stopLoading()
                        mBinding?.tvError?.text = ""
                    }
                }
            }
        }
    }

}