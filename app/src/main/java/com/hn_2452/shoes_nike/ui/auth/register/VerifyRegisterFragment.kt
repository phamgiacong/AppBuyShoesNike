package com.hn_2452.shoes_nike.ui.auth.register

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentVerifyRegisterBinding
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.toDayString
import com.hn_2452.shoes_nike.utility.toTimeString

class VerifyRegisterFragment : BaseFragment<FragmentVerifyRegisterBinding>() {

    companion object {
        const val TAG = "Nike:VerifyRegisterFragment: "
    }

    private lateinit var mCountDownTimer: CountDownTimer


    private val mRegisterViewModel: RegisterViewModel by activityViewModels()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentVerifyRegisterBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLoading(mBinding?.loadingProgress)
        setupBottomBar(false)
        startTimerForCurrentAuthCode()
        setupRegisterAccount()
    }

    private fun setupRegisterAccount() {
        mBinding?.btnVerify?.setOnClickListener {
            mBinding?.tvErrorInVerifyCode?.visibility = View.INVISIBLE
            mCountDownTimer.cancel()
            val authCode = mBinding?.edtVerifyCode?.editText?.text.toString()
            if (authCode.isNotEmpty() && authCode == mRegisterViewModel.mAuthCode) {
                mRegisterViewModel.register().observe(viewLifecycleOwner) { result ->
                    when (result.status) {
                        Status.LOADING -> {
                            startLoading()
                            Log.i(TAG, "setupRegisterAccount: loading...")
                        }

                        Status.SUCCESS -> {
                            stopLoading()
                            Log.i(TAG, "setupRegisterAccount: success: ${result.data}")
                            result.data?.let {
                                if(result.data) {
                                    Log.i(TAG, "setupRegisterAccount: register success")
                                    Toast.makeText(requireContext(), "Đăng ký thành công", Toast.LENGTH_LONG).show()
                                    mNavController?.navigate(R.id.action_verifyRegisterFragment_to_loginFragment)
                                }
                            }
                        }

                        Status.ERROR -> {
                            stopLoading()
                            Log.e(TAG, "setupRegisterAccount: ${result.message}")
                            mBinding?.tvErrorInVerifyCode?.visibility = View.VISIBLE
                            mBinding?.tvErrorInVerifyCode?.text = result.message
                        }
                    }
                }
            } else {
                mBinding?.tvErrorInVerifyCode?.visibility = View.VISIBLE
                mBinding?.tvErrorInVerifyCode?.text = "Mã sai, vui lòng thử lại. Hoặc chờ để lấy mã mới."
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
                mRegisterViewModel.mAuthCode = ""
                mBinding?.tvTimeRemaining?.text = "Mã đã hết hiệu lực. Nhấn gửi lại mã!"
                mBinding?.tvTimeRemaining?.setOnClickListener {
                    mRegisterViewModel.registerAgain().observe(viewLifecycleOwner) { result ->
                        when (result.status) {
                            Status.LOADING -> {
                                startLoading()
                                Log.i(RegisterFragment.TAG, "setupBtnRegister: loading...")
                            }

                            Status.SUCCESS -> {
                                stopLoading()
                                Log.i(RegisterFragment.TAG, "setupBtnRegister: ${result.data}")
                                result.data?.let {
                                    mRegisterViewModel.mAuthCode = result.data
                                }
                                startTimerForCurrentAuthCode()
                            }

                            Status.ERROR -> {
                                stopLoading()
                                Log.e(RegisterFragment.TAG, "setupBtnRegister: ${result.message}")
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
        // Start the timer
        mCountDownTimer.start()
    }


    override fun onStop() {
        super.onStop()
        setupBottomBar(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCountDownTimer.cancel()
    }
}