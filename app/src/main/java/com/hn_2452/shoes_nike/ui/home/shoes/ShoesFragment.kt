package com.hn_2452.shoes_nike.ui.home.shoes

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hn_2452.shoes_nike.BaseFragment
import com.hn_2452.shoes_nike.R
import com.hn_2452.shoes_nike.databinding.FragmentShoesBinding
import com.hn_2452.shoes_nike.ui.home.shoes.shoes_image.ShoesImageAdapter
import com.hn_2452.shoes_nike.utility.Status
import com.hn_2452.shoes_nike.utility.getOriginPrice
import com.hn_2452.shoes_nike.utility.getPrice
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ShoesFragment : BaseFragment<FragmentShoesBinding>() {

    companion object {
        private const val TAG = "Nike:ShoesFragment: "
    }

    private val mShoesViewModel: ShoesViewModel by viewModels()

    private val mArgs: ShoesFragmentArgs by navArgs()

    @Inject
    lateinit var mSizeAdapter: SizeAdapter

    @Inject
    lateinit var mColorAdapter: ColorAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShoesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPlaceHolderLayout()
        setupLoading(mBinding?.loadingProgress)
        setupToolbar(mBinding?.toolBar)
        setupShoesData(mArgs.shoesId)
        setupCurrentShoes()
        setupAddToFavorite()
        setupBtnAddOrderDetailToCart()
        setupBuyNow()
    }

    private fun setupBuyNow() {
        mBinding?.btnBuyNow?.setOnClickListener {
            mShoesViewModel.buyNow().observe(viewLifecycleOwner) {
                val action = ShoesFragmentDirections.actionShoesFragmentToBuyNowFragment(it)
                mNavController?.navigate(action)
            }
        }
    }

    private fun startPlaceHolderLayout() {
        mBinding?.mainLayout?.visibility = View.INVISIBLE
        mBinding?.placeHolderLayout?.visibility = View.VISIBLE
        mBinding?.placeHolderLayout?.startShimmerAnimation()
    }

    private fun stopPlaceHolderLayout() {
        mBinding?.mainLayout?.visibility = View.VISIBLE
        mBinding?.placeHolderLayout?.visibility = View.GONE
        mBinding?.placeHolderLayout?.stopShimmerAnimation()
    }


    private fun setupAddToFavorite() {
        mBinding?.imvFavorite?.setOnClickListener {
            // Todo: add favorite shoes
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setupShoesData(shoesId: String) {
        mShoesViewModel.getShoesById(shoesId).observe(viewLifecycleOwner) {
            it?.let {
                when (it.status) {
                    Status.LOADING -> {
                        Log.i(TAG, "setupShoesData: loading...")
                    }

                    Status.SUCCESS -> {
                        val shoes = it.data
                        shoes?.let {
                            mBinding?.run {
                                mShoesViewModel.setCurrentShoes(it)
                                stopPlaceHolderLayout()
                            }

                        }
                    }

                    Status.ERROR -> {
                        Log.e(TAG, "setupShoesData: ${it.message}")
                        stopPlaceHolderLayout()
                    }
                }
            }
        }
    }

    private fun setupCurrentShoes() {
        mShoesViewModel.mCurrentShoes.observe(viewLifecycleOwner) { shoes ->
            shoes?.let {
                mBinding?.run {
                    viewPagerShoesImage.adapter =
                        ShoesImageAdapter(this@ShoesFragment, shoes.images)
                    autoRunOfferBanner(shoes.images.size)
                    tvTitle.text = shoes.name
                    tvPrice.text = getPrice(shoes)
                    if (shoes.discount > 0) {
                        tvOriginPrice.visibility = View.VISIBLE
                        tvOriginPrice.text = getOriginPrice(shoes)
                    } else {
                        tvOriginPrice.visibility = View.GONE
                    }
                    tvDescription.text = shoes.description
                    sold.text = getString(R.string.shoes_sold, shoes.sold)
                    rate.text = "${shoes.rate} (${shoes.number_of_reviews} đánh giá)"
                    tvWatchReview.setOnClickListener {
                        if(shoes.number_of_reviews > 0) {
                            mNavController?.navigate(
                                ShoesFragmentDirections.actionShoesFragmentToShoesReviewFragment2(shoes._id)
                            )
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.not_yet_review), Toast.LENGTH_SHORT).show()
                        }
                    }
                    setupSizeDataList()
                    setupColorDataList()
                    setupQuantity()
                }
            }
        }
    }


    private fun autoRunOfferBanner(size: Int) {
        val currentIndex = mBinding?.viewPagerShoesImage?.currentItem
        currentIndex?.let {
            var index = 0
            if (currentIndex < size - 1) {
                index = mBinding?.viewPagerShoesImage?.currentItem?.plus(1) ?: 0
            } else if (currentIndex >= size) {
                index = 0
            }

            mBinding?.viewPagerShoesImage?.setCurrentItem(index, false)
            Handler(Looper.getMainLooper()).postDelayed({
                autoRunOfferBanner(size)
            }, 3000L)
        }
    }

    private fun setupBtnAddOrderDetailToCart() {
        mBinding?.btnAddToCart?.setOnClickListener {
            mShoesViewModel.addOrderDetail().observe(viewLifecycleOwner) { result ->
                when (result?.status) {
                    Status.LOADING -> {
                        Log.i(TAG, "setupBtnAddToCar: loading...")
                    }

                    Status.SUCCESS -> {
                        Toast.makeText(
                            requireContext(),
                            "Thêm sản phẩm vào giỏ thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.i(TAG, "setupBtnAddToCar: success ${result.data}")
                        resetShoes()
                    }

                    Status.ERROR -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "setupBtnAddToCar: ${result.message}")
                    }

                    null -> {
                        Log.i(TAG, "setupBtnAddToCar: null")
                    }
                }
            }
        }
    }

    private fun resetShoes() {
        mBinding?.editQuantity?.text = "1"

        val sizeDataList = mutableListOf<Pair<Int, Boolean>>()
        mShoesViewModel.mCurrentShoes.value?.available_sizes?.forEach {
            sizeDataList.add(Pair(it, false))
        }
        mSizeAdapter.setData(sizeDataList)
        mShoesViewModel.mSelectedSize = -1

        val colorDataList = mutableListOf<Pair<String, Boolean>>()
        mShoesViewModel.mCurrentShoes.value?.available_colors?.forEach {
            colorDataList.add(Pair(it, false))
        }
        mColorAdapter.setData(colorDataList)
        mShoesViewModel.mSelectedColor = null
    }

    private fun setupQuantity() {
        mBinding?.btnReduce?.setOnClickListener {
            val currentValue = Integer.valueOf(mBinding?.editQuantity?.text.toString())
            if (currentValue > 1) {
                mBinding?.editQuantity?.text = currentValue.minus(1).toString()
            }
        }

        mBinding?.btnAugment?.setOnClickListener {
            val currentValue = Integer.valueOf(mBinding?.editQuantity?.text.toString())
            if (currentValue <= 30) {
                mBinding?.editQuantity?.text = currentValue.plus(1).toString()
            } else {
                Toast.makeText(
                    this.context,
                    "Please, contact to admin to buy a big amount of shoes",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        mBinding?.editQuantity?.doAfterTextChanged {
            mShoesViewModel.mSelectedNumber = it.toString().toInt()
        }

    }

    private fun setupColorDataList() {
        mShoesViewModel.mCurrentShoes.observe(viewLifecycleOwner) { shoes ->
            shoes?.let {
                mColorAdapter.setOnClick { color ->
                    mShoesViewModel.mSelectedColor = color
                    val updatedColorDataList = mutableListOf<Pair<String, Boolean>>()
                    shoes.available_colors.forEach {
                        if (color == it) {
                            updatedColorDataList.add(Pair(it, true))
                        } else {
                            updatedColorDataList.add(Pair(it, false))
                        }
                    }
                    mColorAdapter.setData(updatedColorDataList)
                }
                val colorDataList = mutableListOf<Pair<String, Boolean>>()
                shoes.available_colors.forEach {
                    colorDataList.add(Pair(it, false))
                }
                mColorAdapter.setData(colorDataList)
                mBinding?.rcvColor?.adapter = mColorAdapter
            }
        }

    }

    private fun setupSizeDataList() {
        mShoesViewModel.mCurrentShoes.observe(viewLifecycleOwner) { shoes ->

            mSizeAdapter.setOnClickListener { size ->
                mShoesViewModel.mSelectedSize = size
                val newSizeDataList = mutableListOf<Pair<Int, Boolean>>()
                shoes.available_sizes.forEach {
                    if (it == size) {
                        newSizeDataList.add(Pair(it, true))
                    } else {
                        newSizeDataList.add(Pair(it, false))
                    }
                }
                mSizeAdapter.setData(newSizeDataList)
            }

            val sizeDataList = mutableListOf<Pair<Int, Boolean>>()
            shoes.available_sizes.forEach {
                sizeDataList.add(Pair(it, false))
            }
            mSizeAdapter.setData(sizeDataList)
            mBinding?.rcvSize?.adapter = mSizeAdapter
        }

    }


}