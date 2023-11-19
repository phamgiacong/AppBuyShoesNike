package com.hn_2452.shoes_nike.ui.login

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.MainActivity
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var loadSignUp: ProgressDialog

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView()
        setOnClickListener()
        observerData()
    }

    private fun bindView() {
        (requireActivity() as MainActivity).hideBottomNav()
        auth = FirebaseAuth.getInstance()
        loadSignUp = ProgressDialog(requireContext())

    }


    private fun setOnClickListener() {
        _mBinding?.tvSignIn?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        _mBinding?.back?.setOnClickListener {
            findNavController().popBackStack()
        }

        _mBinding?.tvRegister?.setOnClickListener {
            validateData()
        }
    }

    private fun observerData() {

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun registerUser(email: String, pass: String) {
        loadSignUp.show()
        loadSignUp.setMessage("In Register ...")
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Register Finish")
                }
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                loadSignUp.dismiss()
            }
    }

    private fun validateData() {
        val email = _mBinding?.edEmail?.editText?.text?.toString()
        val pass = _mBinding?.edPass?.editText?.text?.toString()
        val repass = _mBinding?.edRePass?.editText?.text?.toString()

        when {
            TextUtils.isEmpty(email) -> showToast("Email is required")
            TextUtils.isEmpty(pass) -> showToast("Password is required")
            TextUtils.isEmpty(repass) -> showToast("Re-enter Password is required")
            pass != repass -> showToast("Passwords do not match")
            else -> registerUser(email!!, pass!!)
        }
    }
}