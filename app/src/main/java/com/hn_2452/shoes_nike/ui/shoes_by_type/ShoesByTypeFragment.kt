package com.hn_2452.shoes_nike.ui.shoes_by_type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentShoesByTypeBinding
import com.hn_2452.shoes_nike.ui.ShoesAdapterController
import com.hn_2452.shoes_nike.utility.handleResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoesByTypeFragment : BaseFragment<FragmentShoesByTypeBinding>() {

    private val mArgs: ShoesByTypeFragmentArgs by navArgs()

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
        setupShoesList()
    }

    private fun setupShoesList() {
        val controller = ShoesAdapterController {
            mNavController?.navigate(
                ShoesByTypeFragmentDirections.actionShoesByTypeFragmentToShoesFragment(it._id)
            )
        }
        mBinding?.rcvFavoriteShoes?.setController(controller)
        handleResource(
            data = mShoesByTypeViewModel.loadShoesListByType(mArgs.typeId),
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            onSuccess = { shoesList ->
                stopLoading()
                if (shoesList.isNullOrEmpty()) {
                    mBinding?.noneShoes?.visibility = View.VISIBLE
                } else {
                    mBinding?.noneShoes?.visibility = View.GONE
                }
                controller.setData(shoesList ?: emptyList())
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

}