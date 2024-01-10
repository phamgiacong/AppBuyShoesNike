package com.hn_2452.shoes_nike.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.TOKEN
import com.hn_2452.shoes_nike.databinding.FragmentFavoriteBinding
import com.hn_2452.shoes_nike.ui.ShoesAdapterController
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.getStringDataByKey
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    companion object {
        private const val TAG = "Nike:FavoriteFragment: "
    }

    private val mFavoriteViewModel: FavoriteViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFavoriteBinding.inflate(inflater, container, false)

    override fun onStart() {
        super.onStart()
        setupBottomBar(true)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoginRequestLayout(mBinding?.mainLayout, mBinding?.layoutNeedLogin)
        setupLoading(mBinding?.loadingProgress)
        setupToolbar(mBinding?.toolBar)
        setupBottomBar(true)
        setupFavoriteShoesList()
    }

    private fun setupFavoriteShoesList() {
        val controller = ShoesAdapterController {
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToShoesFragment(it._id)
            mNavController?.navigate(action)
        }
        mBinding?.rcvFavoriteShoes?.setController(controller)
        mFavoriteViewModel.loadFavoriteShoesList().observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result.status) {
                    Status.LOADING -> {
                        startLoading()
                    }

                    Status.SUCCESS -> {
                        stopLoading()
                        if (result.data.isNullOrEmpty()) {
                            mBinding?.noneShoes?.visibility = View.VISIBLE
                        } else {
                            mBinding?.noneShoes?.visibility = View.GONE
                        }
                        controller.setData(result.data ?: emptyList())
                    }

                    Status.ERROR -> {
                        stopLoading()
                        Log.e(TAG, "setupFavoriteShoesList: ${result.message}")
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        setupBottomBar(false)
    }


}