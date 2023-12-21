package com.hn_2452.shoes_nike.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.facebook.FacebookException
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.databinding.FragmentLoginBinding
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import com.hn_2452.shoes_nike.utility.sign_in_util.FacebookSignInComponent
import com.hn_2452.shoes_nike.utility.sign_in_util.GoogleSignInComponent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    companion object {
        const val TAG = "Nike:LoginFragment: "
    }

    private val mLoginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var mGoogleSignInComponent: GoogleSignInComponent

    @Inject
    lateinit var mFacebookSignInComponent: FacebookSignInComponent


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGoogleSignIn()
        setupFacebookSignIn()
        setupLogin()
        navigateToRegister()
        navigateToForgetPassword()
        moveBackWhenAuthozied()
    }

    private fun moveBackWhenAuthozied() {
        if(getStringDataByKey(requireContext(), TOKEN).isNotEmpty()) {
            mNavController?.popBackStack()
        }
    }

    private fun navigateToForgetPassword() {
        mBinding?.tvForgetPassword?.setOnClickListener {
            mNavController?.navigate(R.id.forgetAccountFragment)
        }
    }

    private fun setupGoogleSignIn() {
        mBinding?.imvGoogle?.setOnClickListener {
            mBinding?.tvError?.visibility = View.GONE
            mGoogleSignInComponent.login(
                requireActivity(),
                object : GoogleSignInComponent.Callback {
                    override fun onError(error: Exception) {
                        Log.e(TAG, "onError: ${error.message}")
                    }

                    override fun onSuccess(
                        id: String?,
                        displayName: String?,
                        avatar: String?,
                        email: String?
                    ) {
                        Log.i(TAG, "onSuccess: id=$id")
                        mLoginViewModel.loginWithGoogle(id, displayName, avatar, email)
                            .observe(viewLifecycleOwner) { result ->
                                when (result.status) {
                                    Status.LOADING -> {
                                        Log.i(TAG, "setupGoogleSignIn: loading...")
                                    }

                                    Status.SUCCESS -> {
                                        Log.i(TAG, "setupGoogleSignIn: ${result.data}")
                                        Toast.makeText(
                                            requireContext(),
                                            "Đăng nhập thành công",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                        mNavController?.popBackStack()
                                    }

                                    Status.ERROR -> {
                                        Log.e(TAG, "setupGoogleSignIn: ${result.message}")
                                        mBinding?.tvError?.visibility = View.VISIBLE
                                        mBinding?.tvError?.text = result.message
                                    }
                                }
                            }
                    }

                })
        }
    }

    private fun setupFacebookSignIn() {
        mBinding?.imvFacebook?.setOnClickListener {
            mBinding?.tvError?.visibility = View.GONE
            mFacebookSignInComponent.login(
                requireActivity(),
                object : FacebookSignInComponent.Callback {
                    override fun onCancel() {
                        Log.i(TAG, "onCancel: ")
                    }

                    override fun onError(error: FacebookException) {
                        Log.e(TAG, "onError: ${error.message}")
                    }

                    override fun onSuccess(id: String?, name: String?, avatar: String?) {
                        Log.i(TAG, "onSuccess: id=$id")
                        mLoginViewModel.loginWithFacebook(id, name, avatar)
                            .observe(viewLifecycleOwner) { result ->
                                when (result.status) {
                                    Status.LOADING -> {
                                        Log.i(TAG, "setupFacebookSignIn: loading...")
                                    }

                                    Status.SUCCESS -> {
                                        Log.i(TAG, "setupFacebookSignIn: ${result.data}")
                                        Toast.makeText(
                                            requireContext(),
                                            "Đăng nhập thành công",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        mNavController?.popBackStack()
                                    }

                                    Status.ERROR -> {
                                        Log.e(TAG, "setupFacebookSignIn: ${result.message}")
                                        mBinding?.tvError?.visibility = View.VISIBLE
                                        mBinding?.tvError?.text = result.message
                                    }
                                }
                            }
                    }

                })
        }
    }

    private fun navigateToRegister() {
        mBinding?.tvSignUp?.setOnClickListener {
            mNavController?.navigate(R.id.registerFragment)
        }
    }

    private fun setupLogin() {
        mBinding?.btnSignIn?.setOnClickListener {
            mBinding?.tvError?.visibility = View.GONE
            mLoginViewModel.login(
                mBinding?.edtEmail?.editText?.text.toString(),
                mBinding?.edtPassword?.editText?.text.toString()
            ).observe(viewLifecycleOwner) { result ->
                result?.let {
                    when (result.status) {
                        Status.LOADING -> {
                            Log.i(TAG, "setupLogin: loading...")
                        }

                        Status.SUCCESS -> {
                            Log.i(TAG, "setupLogin: success ${result.data}")
                            Toast.makeText(
                                requireContext(),
                                "Đăng nhập thành công",
                                Toast.LENGTH_LONG
                            ).show()
                            mNavController?.popBackStack()
                        }

                        Status.ERROR -> {
                            Log.e(TAG, "setupLogin: ${result.message}")
                            mBinding?.tvError?.visibility = View.VISIBLE
                            mBinding?.tvError?.text = result.message
                        }
                    }
                }
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mGoogleSignInComponent.mCallbackManager?.onActivityResult(requestCode, resultCode, data)
        mFacebookSignInComponent.mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }


}