package com.hn_2452.shoes_nike.ui.cart.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentAddAddressBinding
import com.hn_2452.shoes_nike.utility.handleResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAddressFragment : BaseFragment<FragmentAddAddressBinding>() {

    private val mAddressViewModel: AddressViewModel by viewModels()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddAddressBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.toolBar)
        mBinding?.btnSave?.setOnClickListener {
            addNewAddress()
        }
    }

    private fun addNewAddress() {
        val type = if (mBinding?.house?.isChecked == true) {
            0
        } else {
            1
        }

        handleResource(
            data = mAddressViewModel.addNewAddress(
                type,
                mBinding?.edtAddress?.editText?.text.toString(),
                mBinding?.ckDefault?.isChecked ?: false,
                mBinding?.edtName?.editText?.text.toString(),
                mBinding?.edtPhoneNumber?.editText?.text?.toString(),
            ),
            lifecycleOwner = viewLifecycleOwner,
            onSuccess = {
                Toast.makeText(requireContext(), "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show()
                mNavController?.popBackStack()
            },
            isErrorInform = true,
            context = requireContext()
        )
    }
}