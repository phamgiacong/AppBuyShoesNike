package com.hn_2452.shoes_nike.ui.cart.address

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentListAddressBinding
import com.hn_2452.shoes_nike.ui.cart.check_out.CheckOutViewModel
import com.hn_2452.shoes_nike.utility.handleResource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddressFragment : BaseFragment<FragmentListAddressBinding>() {

    companion object {
        const val TAG = "Nike:AddressFragment: "
    }

    private val mAddressViewModel: AddressViewModel by viewModels()

    private val mCheckOutViewModel: CheckOutViewModel by activityViewModels()

    @Inject
    lateinit var mAddressAdapter: AddressAdapter


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentListAddressBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(mBinding?.toolBar)
        setupAddressList()
        setupAddNewAddress()
    }

    private fun setupAddNewAddress() {
        mBinding?.addNewAddress?.setOnClickListener {
            mNavController?.navigate(R.id.addAddressFragment)
        }
    }

    private fun setupAddressList() {
        mAddressAdapter.mOnSelect = { address ->
            mCheckOutViewModel.mCurrentAddress.value = address
            mNavController?.popBackStack()
        }
        mAddressAdapter.mOnEdit = { address ->
            mNavController?.navigate(
                AddressFragmentDirections.actionAddressFragmentToEditAddressFragment(
                    address
                )
            )
        }
        mBinding?.rcvAddress?.adapter = mAddressAdapter
        loadAddressList()
    }

    private fun loadAddressList() {
        Log.i(TAG, "loadAddressList: ")
        handleResource(
            data = mAddressViewModel.getAddressOfUser(),
            lifecycleOwner = viewLifecycleOwner,
            onSuccess = { addresses ->
                mAddressAdapter.submitList(addresses)
            },
            isErrorInform = true,
            context = requireContext()
        )
    }


}