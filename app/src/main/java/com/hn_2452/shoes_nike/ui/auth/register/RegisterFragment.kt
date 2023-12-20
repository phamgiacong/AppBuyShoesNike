package com.hn_2452.shoes_nike.ui.auth.register

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentRegisterBinding
import com.hn_2452.shoes_nike.utility.Status
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    companion object {
        const val TAG = "Nike:RegisterFragment: "
    }

    private val mRegisterViewModel: RegisterViewModel by activityViewModels()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLoading(mBinding?.loadingProgress)
        setupPolicy()
        setupBtnRegister()
        setupRegisterInput()
    }

    private fun setupRegisterInput() {
        mRegisterViewModel.mRegisterInfoInput?.let {
            mBinding?.edtEmail?.editText?.setText(it.email)
            mBinding?.edtPassword?.editText?.setText(it.password)
            mBinding?.edtConfirmPassword?.editText?.setText(it.checkerPassword)
            mBinding?.chkPolicy?.isChecked = it.acceptPolicy
        }
    }

    private fun setupPolicy() {
        val spannableString =
            SpannableString("Để đăng ký bạn cần chấp nhận điều khoản của chúng tôi")
        spannableString.setSpan(
            ForegroundColorSpan(Color.DKGRAY),
            28,
            38,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                mNavController?.navigate(R.id.policyFragment)
                mBinding?.chkPolicy?.isChecked = !mBinding?.chkPolicy?.isChecked!!
            }
        }, 28, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        mBinding?.chkPolicy?.text = spannableString
        mBinding?.chkPolicy?.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupBtnRegister() {
        mBinding?.run {
            btnSignUp.setOnClickListener {
                mRegisterViewModel.requestRegister(
                    edtEmail.editText?.text.toString(),
                    edtPassword.editText?.text.toString(),
                    edtConfirmPassword.editText?.text.toString(),
                    chkPolicy.isChecked
                ).observe(viewLifecycleOwner) { result ->
                    when (result.status) {
                        Status.LOADING -> {
                            startLoading()
                            Log.i(TAG, "setupBtnRegister: loading...")
                            mBinding?.tvError?.visibility = View.INVISIBLE
                        }

                        Status.SUCCESS -> {
                            stopLoading()
                            Log.i(TAG, "setupBtnRegister: ${result.data}")
                            result.data?.let {
                                mRegisterViewModel.mAuthCode = result.data
                            }
                            mNavController?.navigate(R.id.verifyRegisterFragment)
                        }

                        Status.ERROR -> {
                            stopLoading()
                            Log.e(TAG, "setupBtnRegister: ${result.message}")
                            mBinding?.tvError?.text = result.message
                            mBinding?.tvError?.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mRegisterViewModel.mRegisterInfoInput = RegisterViewModel.RegisterInfoInput(
            mBinding?.edtEmail?.editText?.text.toString(),
            mBinding?.edtPassword?.editText?.text.toString(),
            mBinding?.edtConfirmPassword?.editText?.text.toString(),
            mBinding?.chkPolicy?.isChecked ?: false
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mRegisterViewModel.mRegisterInfoInput = null
    }


}