package com.hn_2452.shoes_nike.ui.login

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.MainActivity
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentRegisterBinding

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private lateinit var auth: FirebaseAuth
    private lateinit var loadSignIn: ProgressDialog

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindView()
        setOnClickListener()
        observerData()
    }

    private fun bindView() {
        (requireActivity() as MainActivity).hideBottomNav()
        auth = FirebaseAuth.getInstance()
        loadSignIn = ProgressDialog(requireContext())
    }


    private fun setOnClickListener() {
        _mBinding?.back?.setOnClickListener {
            findNavController().popBackStack()
        }

        _mBinding?.tvSignUp?.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        _mBinding?.tvLogin?.setOnClickListener {
            loginData()
        }

        _mBinding?.constraintLayout?.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun observerData() {

    }

    private fun loginData() {
        loadSignIn.show()
        loadSignIn.setMessage("Signing In ...")

        val email = _mBinding?.edEmail?.editText?.text.toString()
        val pass = _mBinding?.edPass?.editText?.text.toString()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(requireContext(), "Email and Pass are required", Toast.LENGTH_LONG)
                .show()
        } else {
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _mBinding?.edEmail?.editText?.text?.clear()
                        _mBinding?.edPass?.editText?.text?.clear()

                        loadSignIn.dismiss()
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment2)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Email and Password are invalid",
                            Toast.LENGTH_LONG
                        ).show()

                        loadSignIn.dismiss()
                    }
                }
        }
    }


    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(_mBinding?.constraintLayout?.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        loadSignIn.dismiss()
    }

}