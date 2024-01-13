package com.hn_2452.shoes_nike.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import coil.load
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentHomeBinding
import com.hn_2452.shoes_nike.ui.ShoesAdapterController
import com.hn_2452.shoes_nike.ui.home.offer.OfferAdapter
import com.hn_2452.shoes_nike.ui.notification.NotificationViewModel
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.getTimeOfDay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object {
        private const val TAG = "Nike:HomeFragment: "
    }

    private var mLoadingTime = 0

    private val mHomeViewModel: HomeViewModel by activityViewModels()
    private val mNotificationViewModel: NotificationViewModel by viewModels()
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun onStart() {
        super.onStart()
        setupBottomBar(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLoadingTime = 0
        if(mHomeViewModel.mOfferList == null) {
            startPlaceHolderLayout()
        } else {
            mLoadingTime = 2
            stopPlaceHolderLayout()
        }
        setupBottomBar(true)
        setupShoesTypeList()
        setupPopularShoesList()
        setupOfferList()
        setupNavigateToNotificationScreen()
        setupSearching()
        updateUserInfo()
        setUpCountNotification()

    }

    private fun startPlaceHolderLayout() {
        mBinding?.mainLayout?.visibility = View.INVISIBLE
        mBinding?.placeHolderLayout?.visibility = View.VISIBLE
        mBinding?.placeHolderLayout?.startShimmerAnimation()
    }

    private fun stopPlaceHolderLayout() {
        mLoadingTime++
        if (mLoadingTime >= 3) {
            mBinding?.mainLayout?.visibility = View.VISIBLE
            mBinding?.placeHolderLayout?.visibility = View.GONE
            mBinding?.placeHolderLayout?.stopShimmerAnimation()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearching() {
        mBinding?.searchBar?.setOnClickListener {
            mNavController?.navigate(R.id.searchFragment)
        }
    }

    private fun setupNavigateToNotificationScreen() {
        mBinding?.imvNotification?.setOnClickListener {
            mNavController?.navigate(R.id.notificationsFragment)
        }
    }

    private fun setupOfferList() {
        if(mHomeViewModel.mOfferList != null) {
            mBinding?.tvSpecialOffer?.visibility = View.VISIBLE
            mHomeViewModel.mOfferList?.let { offerList ->
                Log.i(TAG, "setupOfferList: $offerList")
                mBinding?.viewpagerOffer?.adapter = OfferAdapter(this, offerList)
                autoRunOfferBanner(offerList.size)
            }
            return
        }
        mHomeViewModel.getAvailableOffer().observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        stopPlaceHolderLayout()
                        mBinding?.tvSpecialOffer?.visibility = View.VISIBLE
                        result.data?.let { offerList ->
                            Log.i(TAG, "setupOfferList: $offerList")
                            mBinding?.viewpagerOffer?.adapter = OfferAdapter(this, offerList)
                            autoRunOfferBanner(offerList.size)
                        }
                    }

                    Status.ERROR -> {
                        Log.e(TAG, "setupOfferList: ${result.message}")
                    }
                }
            }
        }
    }

    private fun autoRunOfferBanner(size: Int) {
        val currentIndex = mBinding?.viewpagerOffer?.currentItem
        currentIndex?.let {
            var index = 0
            if (currentIndex < size - 1) {
                index = mBinding?.viewpagerOffer?.currentItem?.plus(1) ?: 0
            } else if (currentIndex >= size) {
                index = 0
            }

            mBinding?.viewpagerOffer?.setCurrentItem(index, false)
            Handler(Looper.getMainLooper()).postDelayed({
                autoRunOfferBanner(size)
            }, 3000L)
        }

    }

    private fun setupShoesTypeList() {
        val controller = ShoesTypeAdapterController {
            mNavController?.navigate(
                HomeFragmentDirections.actionHomeFragmentToShoesByTypeFragment(it.name, it.id)
            )
        }
        mBinding?.rcvShoesType?.setController(controller)
        if(mHomeViewModel.mShoesTypeList != null) {
            mBinding?.tvShoesType?.visibility = View.VISIBLE
            controller.setData(mHomeViewModel.mShoesTypeList)
            return
        }
        mHomeViewModel.getShoesType().observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        stopPlaceHolderLayout()
                        mBinding?.tvShoesType?.visibility = View.VISIBLE
                        controller.setData(result.data)
                    }

                    Status.ERROR -> {
                        Log.e(TAG, "setupShoesTypeList: ${result.message}")
                    }
                }
            }
        }
    }

    private fun setupPopularShoesList() {
        val controller = ShoesAdapterController {
            val action = HomeFragmentDirections.actionHomeFragmentToShoesFragment(it._id)
            mNavController?.navigate(action)
        }
        mBinding?.rcvPopularShoes?.setController(controller)
        if(mHomeViewModel.mShoesList != null) {
            mBinding?.tvPopularShoes?.visibility = View.VISIBLE
            controller.setData(mHomeViewModel.mShoesList)
            return
        }
        mHomeViewModel.getPopularShoes().observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        stopPlaceHolderLayout()
                        mBinding?.tvPopularShoes?.visibility = View.VISIBLE
                        controller.setData(result.data)
                    }

                    Status.ERROR -> {
                        Log.e(TAG, "setupPopularShoesList: ${result.message}")
                    }
                }
            }
        }
    }

    private fun updateUserInfo() {
        mBinding?.userBar?.visibility = View.GONE
        mHomeViewModel.mUsers.observe(viewLifecycleOwner) { users ->
            if (users != null && users.isNotEmpty()) {
                val user = users[0]
                mBinding?.imvUser?.load(user.avatar) {
                    placeholder(R.drawable.user_placeholder)
                    error(R.drawable.user_placeholder)
                }

                if (user.fullName.isNullOrEmpty()) {
                    mBinding?.tvUserName?.text = user.name
                } else {
                    mBinding?.tvUserName?.text = user.fullName
                }

                mBinding?.tvHello?.text = getTimeOfDay()
                mBinding?.userBar?.visibility = View.VISIBLE
            } else {
                Log.i(TAG, "updateUserInfo: user is null")
                mBinding?.userBar?.visibility = View.GONE
            }
        }
    }
    private fun setUpCountNotification() {
        mHomeViewModel.mUsers.observe(viewLifecycleOwner){users->
            if (users != null && users.isNotEmpty()) {
                val user = users[0]
                mNotificationViewModel.getQuantityNotification(user.id).observe(viewLifecycleOwner){resources->
                    resources?.let {
                        when(resources.status){
                            Status.LOADING ->{

                            }Status.SUCCESS ->{
                                resources.data?.let {
                                    it.data?.let {
                                        if(it>0){
                                            mBinding?.notificationNumber?.visibility=View.VISIBLE
                                            mBinding?.txtNotification?.text=it.toString()
                                        }else{
                                            mBinding?.notificationNumber?.visibility=View.GONE
                                        }
                                    }
                                }

                            }Status.ERROR ->{

                            }
                        }
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