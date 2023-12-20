package com.hn_2452.shoes_nike.ui.auth.forget_account

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentForgetAccountBinding
import com.hn_2452.shoes_nike.ui.auth.register.RegisterFragment
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.toTimeString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetAccountFragment: BaseFragment<FragmentForgetAccountBinding>() {

    companion object {
        const val TAG = "Nike:ForgetAccountFragment: "
    }

    private lateinit var mCountDownTimer: CountDownTimer

    private val mForgetAccountViewModel: ForgetAccountViewModel by viewModels()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentForgetAccountBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomBar(false)
        setupLoading(mBinding?.loadingProgress)
        setupFindByEmail()
        setupConfirmAuthCode()
    }

    private fun setupConfirmAuthCode() {
        mBinding?.btnConfirm?.setOnClickListener {
            mBinding?.tvError?.text = ""
            mForgetAccountViewModel.changePasswordByCode(
                mBinding?.edtCode?.editText?.text.toString(),
                mBinding?.edtNewPassword?.editText?.text.toString(),
                mBinding?.edtReNewPassword?.editText?.text.toString()
            ).observe(viewLifecycleOwner) { result ->
                when(result?.status) {
                    Status.LOADING -> {
                        Log.i(TAG, "setupConfirmAuthCode: loading...")
                        startLoading()
                    }

                    Status.SUCCESS -> {
                        stopLoading()
                        Log.i(TAG, "setupConfirmAuthCode: success")
                        Toast.makeText(requireContext(), "Đổi mật khẩu thành công", Toast.LENGTH_LONG).show()
                        mNavController?.popBackStack()
                    }

                    Status.ERROR -> {
                        stopLoading()
                        Log.e(TAG, "setupConfirmAuthCode: ${result.message}")
                        mBinding?.tvError?.text = result.message
                    }

                    null -> {
                        stopLoading()
                        Log.i(TAG, "setupConfirmAuthCode: result is null")
                    }

                }
            }
        }
    }

    private fun startTimerForCurrentAuthCode() {
        mBinding?.tvTimeRemaining?.text =
            "Mã chỉ còn hiệu lực trong ${(1000 * 60 * 5).toLong().toTimeString()}"
        mBinding?.tvTimeRemaining?.setOnClickListener(null)
        mCountDownTimer = object : CountDownTimer(1000 * 60 * 5, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the TextView with the remaining time
                mBinding?.tvTimeRemaining?.text =
                    "Mã chỉ còn hiệu lực trong ${millisUntilFinished.toTimeString()}"
            }

            override fun onFinish() {
                // Handle what happens when the timer finishes
                mBinding?.tvTimeRemaining?.text = "Mã đã hết hiệu lực. Nhấn gửi lại mã!"
                mBinding?.tvTimeRemaining?.setOnClickListener {
                    mForgetAccountViewModel.sendCodeAgain().observe(viewLifecycleOwner) { result ->
                        when (result.status) {
                            Status.LOADING -> {
                                startLoading()
                                Log.i(RegisterFragment.TAG, "setupBtnRegister: loading...")
                            }

                            Status.SUCCESS -> {
                                stopLoading()
                                Log.i(RegisterFragment.TAG, "setupBtnRegister: ${result.data}")
                                startTimerForCurrentAuthCode()
                            }

                            Status.ERROR -> {
                                stopLoading()
                                Log.e(RegisterFragment.TAG, "setupBtnRegister: ${result.message}")
                                mBinding?.tvError?.text = result.message
                            }
                        }
                    }
                }
            }
        }
        // Start the timer
        mCountDownTimer.start()
    }

    private fun setupFindByEmail() {
        mBinding?.layoutNewPasswordInput?.visibility = View.GONE
        mBinding?.btnSearch?.setOnClickListener {
            mBinding?.layoutNewPasswordInput?.visibility = View.GONE
            mForgetAccountViewModel.findPasswordByEmail(
                mBinding?.edtEmail?.editText?.text.toString()
            ).observe(viewLifecycleOwner) { result ->
                when(result?.status) {
                    Status.LOADING -> {
                        Log.i(TAG, "setupFindByEmail: loading...")
                        startLoading()
                    }
                    Status.SUCCESS -> {
                        stopLoading()
                        Log.i(TAG, "setupFindByEmail: success ${result.data}")
                        mBinding?.tvError?.text = ""
                        mBinding?.layoutNewPasswordInput?.visibility = View.VISIBLE
                        startTimerForCurrentAuthCode()
                    }
                    Status.ERROR -> {
                        stopLoading()
                        Log.e(TAG, "setupFindByEmail: error ${result.message}")
                        mBinding?.layoutNewPasswordInput?.visibility = View.GONE
                        mBinding?.tvErrorForFindPasswordByEmail?.text = result.message
                    }

                    null -> {
                        stopLoading()
                        mBinding?.layoutNewPasswordInput?.visibility = View.GONE
                        Log.i(TAG, "setupFindByEmail: result is null")
                    }
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        setupBottomBar(true)
    }
}