package com.hn_2452.shoes_nike.ui.cart.address

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.data.model.Address
import com.hn_2452.shoes_nike.databinding.FragmentEditAddressBinding
import com.hn_2452.shoes_nike.utility.handleResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditAddressFragment : BaseFragment<FragmentEditAddressBinding>() {

    private val mAddressViewModel: AddressViewModel by viewModels()

    private val mArgs: EditAddressFragmentArgs by navArgs()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentEditAddressBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.toolBar)
        setupAddress()
        updateAddress()
        deleteAddress()
    }

    private fun deleteAddress() {
        mBinding?.deleteAddress?.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.delete_address_message))
                .setPositiveButton(
                    R.string.delete
                ) { dialog, _ ->
                    handleResource(
                        data = mAddressViewModel.deleteAddress(),
                        lifecycleOwner = viewLifecycleOwner,
                        context = requireContext(),
                        isErrorInform = true,
                        onSuccess = {
                            Toast.makeText(
                                requireContext(),
                                R.string.delete_address_success_message,
                                Toast.LENGTH_SHORT
                            ).show()
                            mNavController?.popBackStack()
                        }
                    )
                    dialog.dismiss()
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun setupAddress() {
        val address: Address = mArgs.getAddress()
        mAddressViewModel.mCurrentAddress = address
        mBinding?.edtAddress?.editText?.setText(address.address)
        mBinding?.edtName?.editText?.setText(address.name)
        mBinding?.edtPhoneNumber?.editText?.setText(address.phoneNumber)
        if (address.type == 0) {
            mBinding?.house?.isChecked = true
        } else {
            mBinding?.office?.isChecked = true
        }

        if (address.default) {
            mBinding?.ckDefault?.isChecked = true
        }
    }

    private fun updateAddress() {
        mBinding?.btnSave?.setOnClickListener {
            val type = if (mBinding?.house?.isChecked == true) {
                0
            } else {
                1
            }

            handleResource(
                data = mAddressViewModel.updateAddress(
                    type,
                    mBinding?.edtAddress?.editText?.text.toString(),
                    mBinding?.ckDefault?.isChecked ?: false,
                    mBinding?.edtName?.editText?.text.toString(),
                    mBinding?.edtPhoneNumber?.editText?.text?.toString(),
                ),
                lifecycleOwner = viewLifecycleOwner,
                onSuccess = {
                    Toast.makeText(
                        requireContext(),
                        "Chỉnh sửa địa chỉ thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                    mNavController?.popBackStack()
                },
                isErrorInform = true,
                context = requireContext()
            )

        }
    }

}