package com.hn_2452.shoes_nike.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.databinding.FragmentFavoriteBinding
import com.hn_2452.shoes_nike.ui.home.ShoesAdapterController
import com.hn_2452.shoes_nike.utility.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    companion object {
        private const val TAG = "Nike:FavoriteFragment: "
    }

    private val mFavoriteViewModel : FavoriteViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentFavoriteBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFavoriteShoesList()
    }

    private fun setupFavoriteShoesList() {
        val controller = ShoesAdapterController {

        }
        mBinding?.rcvFavoriteShoes?.setController(controller)
        mFavoriteViewModel.loadFavoriteShoesList().observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        controller.setData(result.data)
                    }

                    Status.ERROR -> {
                        Log.e(TAG, "setupFavoriteShoesList: ${result.message}")
                    }
                }
            }
        }
    }


}