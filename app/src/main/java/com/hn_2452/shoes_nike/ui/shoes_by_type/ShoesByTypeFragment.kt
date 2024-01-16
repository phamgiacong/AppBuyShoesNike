package com.hn_2452.shoes_nike.ui.shoes_by_type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentShoesByTypeBinding
import com.hn_2452.shoes_nike.ui.ShoesAdapterController
import com.hn_2452.shoes_nike.utility.handleResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoesByTypeFragment : BaseFragment<FragmentShoesByTypeBinding>() {

    private val mArgs: ShoesByTypeFragmentArgs by navArgs()

    lateinit var mController: ShoesAdapterController

    private val mShoesByTypeViewModel: ShoesByTypeViewModel by viewModels()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShoesByTypeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLoading(mBinding?.loadingProgress)
        mBinding?.toolBar?.setTitle(mArgs.name)
        setupToolbar(mBinding?.toolBar)
        setupSort()
        setupShoesList()
    }

    private fun setupSort() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.sort_shoes, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding?.spinner?.adapter = adapter
        mBinding?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        mShoesByTypeViewModel.mSort = 0
                        mController.setData(
                            mShoesByTypeViewModel.mShoesList?.sortedByDescending { it.created_date }
                        )
                    }

                    1 -> {
                        mShoesByTypeViewModel.mSort = 1
                        mController.setData(
                            mShoesByTypeViewModel.mShoesList?.sortedByDescending { it.finalPrice }
                        )
                    }

                    2 -> {
                        mShoesByTypeViewModel.mSort = 2
                        mController.setData(
                            mShoesByTypeViewModel.mShoesList?.sortedBy { it.finalPrice }
                        )
                    }

                    3 -> {
                        mShoesByTypeViewModel.mSort = 3
                        mController.setData(
                            mShoesByTypeViewModel.mShoesList?.sortedByDescending { it.sold }
                        )
                    }

                    4 -> {
                        mShoesByTypeViewModel.mSort = 4
                        mController.setData(
                            mShoesByTypeViewModel.mShoesList?.sortedByDescending { it.rate }
                        )
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }

    private fun setupShoesList() {
        mController = ShoesAdapterController {
            mShoesByTypeViewModel.mNeedToLoadOldData = true
            mNavController?.navigate(
                ShoesByTypeFragmentDirections.actionShoesByTypeFragmentToShoesFragment(it._id)
            )
        }
        mBinding?.rcvFavoriteShoes?.setController(mController)
        handleResource(
            data = mShoesByTypeViewModel.loadShoesListByType(mArgs.typeId),
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            onSuccess = { shoesList ->
                mShoesByTypeViewModel.mShoesList = shoesList
                stopLoading()
                if (shoesList.isNullOrEmpty()) {
                    mBinding?.noneShoes?.visibility = View.VISIBLE
                } else {
                    mBinding?.noneShoes?.visibility = View.GONE
                }

                if(mShoesByTypeViewModel.mNeedToLoadOldData) {
                    mBinding?.spinner?.setSelection(mShoesByTypeViewModel.mSort)
                } else {
                    mController.setData(mShoesByTypeViewModel.mShoesList)
                    mBinding?.spinner?.setSelection(0)
                }
            },
            isErrorInform = true,
            onLoading = {
                startLoading()
            },
            onError = {
                stopLoading()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mShoesByTypeViewModel.mSort = 0
        mShoesByTypeViewModel.mNeedToLoadOldData = false
    }

}